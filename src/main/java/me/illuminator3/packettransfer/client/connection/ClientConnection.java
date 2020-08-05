package me.illuminator3.packettransfer.client.connection;

import me.illuminator3.packettransfer.client.connection.packet.ClientPacketChannel;
import me.illuminator3.packettransfer.client.core.PacketClient;
import me.illuminator3.packettransfer.packet.PacketChannel;

import java.io.IOException;

public class ClientConnection
{
    private final PacketClient client;

    private boolean open;
    public PacketChannel channel;

    public ClientConnection(PacketClient client)
    {
        this.client = client;
    }

    public void connect()
        throws IOException
    {
        channel = new ClientPacketChannel(client);

        channel.open();

        open = true;
    }

    public void close()
        throws IOException
    {
        if (!open)
            throw new IOException("Client is already disconnected");

        open = false;

        channel.close();
    }

    public boolean isOpen()
    {
        return open;
    }
}