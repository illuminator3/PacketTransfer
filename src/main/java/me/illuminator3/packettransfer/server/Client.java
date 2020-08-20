package me.illuminator3.packettransfer.server;

import me.illuminator3.packettransfer.packet.PacketChannel;
import me.illuminator3.packettransfer.server.connection.ClientChannel;
import me.illuminator3.packettransfer.server.impl.ServerImpl;

import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    private final InetAddress address;
    private final PacketChannel channel;
    private final Socket socket;

    public Client(InetAddress address, Socket socket, ServerImpl server)
    {
        this.address = address;
        this.channel = new ClientChannel(this, server);
        this.socket = socket;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public PacketChannel getChannel()
    {
        return channel;
    }

    public Socket getSocket()
    {
        return socket;
    }
}