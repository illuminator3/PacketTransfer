package me.illuminator3.packettransfer.server.core;

import me.illuminator3.packettransfer.packet.*;
import me.illuminator3.packettransfer.packet.pre.SimplePacketDecoder;
import me.illuminator3.packettransfer.packet.pre.SimplePacketEncoder;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.impl.ServerImpl;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketServer
    implements ServerImpl
{
    private final int port;
    private boolean connected;
    private PacketHandler packetHandler;
    private PacketEncoder encoder;
    private PacketDecoder decoder;
    private final List<Client> connectedClients = new ArrayList<>();
    private final List<Consumer<Client>> connectListeners = new ArrayList<>();
    private final List<Function<Client, Boolean>> disconnectHandlers = new ArrayList<>();
    private Thread readThread;
    private final Runnable defaultRead;

    public PacketServer(int port)
    {
        this.port = port;

        this.encoder = new SimplePacketEncoder();
        this.decoder = new SimplePacketDecoder();

        this.defaultRead = () -> {
            try
            {
                ServerSocket server = new ServerSocket(port);
                ExecutorService service = Executors.newCachedThreadPool();

                //noinspection InfiniteLoopStatement
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
        };
    }

    @Override
    public boolean disconnectMe(Client me)
    {
        AtomicBoolean disconnect = new AtomicBoolean(true);

        this.disconnectHandlers.forEach(f -> {
            if (f.apply(me))
                disconnect.set(false);
        });

        if (disconnect.get())
            this.connectedClients.remove(me);

        return disconnect.get();
    }

    @Override
    public void disconnect()
        throws IOException
    {
        if (!isConnected())
            connected = false;
        else
            throw new IllegalStateException("Server is not connected");

        for (Client client : connectedClients)
            client.getChannel().close();

        this.readThread.interrupt();
        this.readThread = null;

        System.gc();
    }

    @Override
    public boolean isConnected()
    {
        return this.connected;
    }

    @Override
    public void setPacketHandler(PacketHandler handler)
    {
        this.packetHandler = handler;

        this.packetHandler.registerPackets();
    }

    @Override
    public PacketHandler getPacketHandler()
    {
        return packetHandler;
    }

    @Override
    public void connect()
        throws IOException
    {
        if (packetHandler == null)
            throw new IOException("Can't connect without a packet handler");

        if (this.isConnected())
            throw new IOException("Already connected");

        this.connected = true;

        this.readThread = new Thread(defaultRead);
        this.readThread.start();
    }

    @Override
    public void onConnect(Consumer<Client> consumer)
    {
        this.connectListeners.add(consumer);
    }

    @Override
    public void addDisconnectHandler(Function<Client, Boolean> handler)
    {
        this.disconnectHandlers.add(handler);
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    public PacketEncoder getEncoder()
    {
        return encoder;
    }

    @Override
    public void setEncoder(PacketEncoder encoder)
    {
        this.encoder = encoder;
    }

    @Override
    public PacketDecoder getDecoder()
    {
        return decoder;
    }

    @Override
    public void setDecoder(PacketDecoder decoder)
    {
        this.decoder = decoder;
    }

    @Override
    public List<Client> getConnectedClients()
    {
        return connectedClients;
    }
}