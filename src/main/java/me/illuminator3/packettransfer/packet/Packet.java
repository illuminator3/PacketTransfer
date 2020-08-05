package me.illuminator3.packettransfer.packet;

import me.illuminator3.packettransfer.client.core.PacketClient;
import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.core.PacketServer;

import java.io.IOException;

public interface Packet
{
    void readData(DataReader reader) throws IOException;
    void writeData(DataWriter writer) throws IOException;
    void handleClientSide(PacketClient client);
    void handleServerSide(Client client, PacketServer server);
}