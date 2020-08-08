package me.illuminator3.packettransfer.server.connection;

import me.illuminator3.packettransfer.packet.Packet;
import me.illuminator3.packettransfer.packet.PacketChannel;
import me.illuminator3.packettransfer.packet.PacketEncoder;
import me.illuminator3.packettransfer.packet.PacketHandler;
import me.illuminator3.packettransfer.packet.data.PacketDataWriter;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.core.PacketServer;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClientChannel
    implements PacketChannel
{
    private final Client client;
    private final PacketServer server;
    private DataOutputStream dataOutputStream;

    public ClientChannel(Client client, PacketServer server)
    {
        this.client = client;
        this.server = server;
    }

    public void write(Packet packet)
        throws IOException
    {
        if (this.dataOutputStream == null)
            this.dataOutputStream = new DataOutputStream(client.getSocket().getOutputStream());

        PacketDataWriter writer = new PacketDataWriter();

        packet.writeData(writer);

        PacketHandler handler = server.getPacketHandler();
        int id = handler.getId(packet);

        if (id == -1)
            throw new IllegalArgumentException("Cannot serialize unregistered packet");

        PacketEncoder encoder = server.getEncoder();
        String s = encoder.encode(writer, id);

        dataOutputStream.writeUTF(s);
    }

    public void open()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Not supported");
    }

    public void close()
        throws IOException
    {
        this.dataOutputStream.close();
        this.client.getSocket().close();

        this.server.disconnectMe(this.client);
    }
}