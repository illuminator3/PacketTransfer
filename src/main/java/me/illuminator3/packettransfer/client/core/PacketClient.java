package me.illuminator3.packettransfer.client.core;

import me.illuminator3.packettransfer.client.connection.ClientConnection;
import me.illuminator3.packettransfer.client.impl.ClientImpl;
import me.illuminator3.packettransfer.client.impl.Connection;
import me.illuminator3.packettransfer.packet.PacketDecoder;
import me.illuminator3.packettransfer.packet.PacketEncoder;
import me.illuminator3.packettransfer.packet.PacketHandler;
import me.illuminator3.packettransfer.packet.pre.SimplePacketDecoder;
import me.illuminator3.packettransfer.packet.pre.SimplePacketEncoder;
import me.illuminator3.packettransfer.utils.exceptions.ConnectionError;

import java.io.IOException;

public class PacketClient
    implements ClientImpl
{
    private final String host;
    private final int port;

    private boolean connected;
    private Connection connection;
    private PacketHandler packetHandler;
    private PacketEncoder encoder;
    private PacketDecoder decoder;

    public PacketClient(String host, int port)
    {
        this.host = host;
        this.port = port;

        this.encoder = new SimplePacketEncoder();
        this.decoder = new SimplePacketDecoder();
    }

    @Override
    public void disconnect()
        throws IOException
    {
        if (isConnected())
        {
            connected = false;

            connection.close();
        }
        else
            throw new IOException("Client is already disconnected");
    }

    @Override
    public boolean isConnected()
    {
        if (this.connected && this.connection == null)
            throw new ConnectionError("Client is connected but connection is null");
        else if (!this.connected && this.connection != null)
            this.connection = null;

        return this.connected;
    }

    @Override
    public Connection getConnection()
    {
        return connection;
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

        this.connection = new ClientConnection(this);

        this.connection.connect();

        this.connected = true;
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    public String getHost()
    {
        return host;
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
}