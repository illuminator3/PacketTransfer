package me.illuminator3.packettransfer.client.connection;

import me.illuminator3.packettransfer.client.connection.packet.ClientPacketChannel;
import me.illuminator3.packettransfer.client.impl.ClientImpl;
import me.illuminator3.packettransfer.client.impl.Connection;
import me.illuminator3.packettransfer.packet.PacketChannel;

import java.io.IOException;

public class ClientConnection
    extends Connection
{
    private final ClientImpl client;

    private boolean open;
    public PacketChannel channel;

    public ClientConnection(ClientImpl client)
    {
        this.client = client;
    }

    @Override
    public void connect()
        throws IOException
    {
        channel = new ClientPacketChannel(client);

        channel.open();

        open = true;
    }

    @Override
    public void close()
        throws IOException
    {
        if (!open)
            throw new IOException("Client is already disconnected");

        open = false;

        channel.close();
    }

    @Override
    public boolean isOpen()
    {
        return open;
    }
}