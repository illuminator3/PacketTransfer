package me.illuminator3.packettransfer.test.client;

import me.illuminator3.packettransfer.client.core.PacketClient;
import me.illuminator3.packettransfer.client.impl.ClientImpl;
import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.packet.Packet;
import me.illuminator3.packettransfer.packet.pre.PrePacketHandler;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.impl.ServerImpl;

import java.io.IOException;

class ClientTest
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Starting...");

        ClientImpl client = new PacketClient("localhost", 7664);

        PrePacketHandler packetHandler = new PrePacketHandler();

        packetHandler.addPacket(0x01, new MessagePacket());

        client.setPacketHandler(packetHandler);

        System.out.println("Connecting...");

        client.connect();

        System.out.println("Sending packet...");

        client.getConnection().channel.write(new MessagePacket("hey im a client"));

//        System.out.println("Disconnecting...");
//
//        client.disconnect();
//
//        System.exit(-1);
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
        public void readData(DataReader reader)
        {
            this.message = reader.readString();
        }

        @Override
        public void writeData(DataWriter writer)
        {
            writer.writeString(message);
        }

        @Override
        public void handleClientSide(ClientImpl client)
        {
            System.out.println(message);
        }

        @Override
        public void handleServerSide(Client client, ServerImpl server)
        {
            System.out.println(message);
        }
    }
}