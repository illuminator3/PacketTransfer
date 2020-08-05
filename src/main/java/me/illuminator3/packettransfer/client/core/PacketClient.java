package me.illuminator3.packettransfer.client.core;

import me.illuminator3.packettransfer.client.connection.ClientConnection;
import me.illuminator3.packettransfer.packet.PacketDecoder;
import me.illuminator3.packettransfer.packet.PacketEncoder;
import me.illuminator3.packettransfer.packet.PacketHandler;
import me.illuminator3.packettransfer.packet.pre.SimplePacketDecoder;
import me.illuminator3.packettransfer.packet.pre.SimplePacketEncoder;
import me.illuminator3.packettransfer.utils.exceptions.ConnectionError;

import java.io.IOException;

public class PacketClient
{
    private final String host;
    private final int port;

    private boolean connected;
    private ClientConnection connection;
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

    public boolean isConnected()
    {
        if (this.connected && this.connection == null)
            throw new ConnectionError("Client is connected but connection is null");
        else if (!this.connected && this.connection != null)
            this.connection = null;

        return this.connected;
    }

    public ClientConnection getConnection()
    {
        return connection;
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

        this.connection = new ClientConnection(this);

        this.connection.connect();

        this.connected = true;
    }

    public int getPort()
    {
        return port;
    }

    public String getHost()
    {
        return host;
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
}