package me.illuminator3.packettransfer.test.server;

import me.illuminator3.packettransfer.client.core.PacketClient;
import me.illuminator3.packettransfer.packet.pre.PrePacketHandler;
import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.packet.Packet;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.core.PacketServer;

import java.io.IOException;

class ServerTest
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Starting...");

        PacketServer server = new PacketServer(7664);

        PrePacketHandler packetHandler = new PrePacketHandler();

        packetHandler.addPacket(0x01, new MessagePacket());

        server.setPacketHandler(packetHandler);

        System.out.println("Connecting...");

        server.connect();

        server.onConnect(client -> {
            try
            {
                client.getChannel().write(new MessagePacket("hi im the server"));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    static class MessagePacket
        implements Packet
    {
        private String message = null;

        public MessagePacket()
        {

        }

        public MessagePacket(String message)
        {
            this.message = message;
        }

        @Override
        public void readData(DataReader reader) throws IOException
        {
            this.message = reader.readString();
        }

        @Override
        public void writeData(DataWriter writer) throws IOException
        {
            writer.writeString(message);
        }

        @Override
        public void handleClientSide(PacketClient client)
        {
            System.out.println(message);
        }

        @Override
        public void handleServerSide(Client client, PacketServer server)
        {
            System.out.println(message);
        }
    }
}