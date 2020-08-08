package me.illuminator3.packettransfer.server.core;

import me.illuminator3.packettransfer.packet.*;
import me.illuminator3.packettransfer.packet.pre.SimplePacketDecoder;
import me.illuminator3.packettransfer.packet.pre.SimplePacketEncoder;
import me.illuminator3.packettransfer.server.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PacketServer
{
    private final int port;
    private boolean connected;
    private PacketHandler packetHandler;
    private PacketEncoder encoder;
    private PacketDecoder decoder;
    private final List<Client> connectedClients = new ArrayList<>();
    private final List<Consumer<Client>> connectListeners = new ArrayList<>();

    public PacketServer(int port)
    {
        this.port = port;

        this.encoder = new SimplePacketEncoder();
        this.decoder = new SimplePacketDecoder();
    }

    public void disconnect()
        throws IOException
    {
        if (!isConnected())
        {
            connected = false;
        }
        else
            throw new IOException("Server is already disconnected");

        Runtime.getRuntime().exit(0);
    }

    public boolean isConnected()
    {
        return this.connected;
    }

    public void setPacketHandler(PacketHandler handler)
    {
        this.packetHandler = handler;

        this.packetHandler.registerPackets();
    }

    public PacketHandler getPacketHandler()
    {
        return packetHandler;
    }

    public void connect()
        throws IOException
    {
        if (packetHandler == null)
            throw new IOException("Can't connect without a packet handler");

        if (this.isConnected())
            throw new IOException("Already connected");

        this.connected = true;

        new Thread(() -> {
            try
            {
                ServerSocket server = new ServerSocket(port);
                ExecutorService service = Executors.newCachedThreadPool();

                while (true)
                {
                    Socket s = server.accept();

                    service.submit(() -> {
                        Client client = new Client(s.getInetAddress(), s, this);

                        connectedClients.add(client);

                        this.connectListeners.forEach(consumer -> consumer.accept(client));

                        try
                        {
                            DataInputStream datain = new DataInputStream(s.getInputStream());

                            while (true)
                            {
                                String read = datain.readUTF();

                                DecodedPacket dec = decoder.decode(read);

                                Class<? extends Packet> pclass = packetHandler.getPacketById(dec.getId());

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

                                    break;
                                }

                                packet.readData(dec.getData());

                                packet.handleServerSide(client, this);
                            }
                        } catch (IOException ex)
                        {
                            try
                            {
                                client.getSocket().close();
                            } catch (IOException ignored) {}

                            connectedClients.remove(client);
                        }
                    });
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    public void onConnect(Consumer<Client> consumer)
    {
        this.connectListeners.add(consumer);
    }

    public int getPort()
    {
        return port;
    }

    public PacketEncoder getEncoder()
    {
        return encoder;
    }

    public void setEncoder(PacketEncoder encoder)
    {
        this.encoder = encoder;
    }

    public PacketDecoder getDecoder()
    {
        return decoder;
    }

    public void setDecoder(PacketDecoder decoder)
    {
        this.decoder = decoder;
    }

    public List<Client> getConnectedClients()
    {
        return connectedClients;
    }
}