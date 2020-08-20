package me.illuminator3.packettransfer.client.connection.packet;

import me.illuminator3.packettransfer.client.impl.ClientImpl;
import me.illuminator3.packettransfer.packet.*;
import me.illuminator3.packettransfer.packet.data.PacketDataWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientPacketChannel
    implements PacketChannel
{
    private final ClientImpl client;
    private boolean open;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private Thread readThread;

    public ClientPacketChannel(ClientImpl client)
    {
        this.client = client;
    }

    @Override
    public void write(Packet packet)
        throws IOException
    {
        PacketDataWriter writer = new PacketDataWriter();

        packet.writeData(writer);

        PacketHandler handler = client.getPacketHandler();
        int id = handler.getId(packet);

        if (id == -1)
            throw new IllegalArgumentException("Cannot serialize unregistered packet");

        PacketEncoder encoder = client.getEncoder();
        String s = encoder.encode(writer, id);

        dataOutputStream.writeUTF(s);
    }

    @Override
    public void open()
        throws IOException
    {
        this.open(5000);
    }

    public void open(int timeout)
        throws IOException
    {
        if (open)
            throw new IOException("Already connected");

        open = true;

        socket = new Socket();

        socket.connect(new InetSocketAddress(client.getHost(), client.getPort()), timeout);

        socket.setKeepAlive(true);

        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        readThread = new Thread(() -> {
            DataInputStream datain;

            try
            {
                datain = new DataInputStream(socket.getInputStream());
            } catch (IOException e)
            {
                try
                {
                    close();
                } catch (IOException ignored) {}

                return;
            }

            try
            {
                while (true)
                {
                    String read = datain.readUTF();

                    DecodedPacket dec = client.getDecoder().decode(read);

                    Class<? extends Packet> pclass = client.getPacketHandler().getPacketById(dec.getId());

                    Packet packet = null;

                    try
                    {
                        for (Constructor<?> con : pclass.getDeclaredConstructors())
                        {
                            con.setAccessible(true);

                            try
                            {
                                packet = (Packet) con.newInstance();

                                break;
                            } catch (Throwable ignored) {}
                        }

                        if (packet == null)
                            throw new NoSuchMethodException("No default constructor defined for packet " + dec.getId());
                    } catch (NoSuchMethodException ex)
                    {
                        ex.printStackTrace();

                        continue;
                    }

                    packet.readData(dec.getData());

                    packet.handleClientSide(this.client);
                }
            } catch (IOException ex)
            {
                try
                {
                    close();
                } catch (IOException ignored) {}
            }
        });

        readThread.start();
    }

    @Override
    public void close()
        throws IOException
    {
        if (!open)
            throw new IOException("Not connected");

        open = false;

        dataOutputStream.close();
        socket.close();
        readThread.interrupt();

        dataOutputStream = null;
        socket = null;
        readThread = null;
    }

    public boolean isOpen()
    {
        return open;
    }
}