package me.illuminator3.packettransfer.packet;

import me.illuminator3.packettransfer.client.impl.ClientImpl;
import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.server.Client;
import me.illuminator3.packettransfer.server.impl.ServerImpl;

import java.io.IOException;

public interface Packet
{
    void readData(DataReader reader) throws IOException;
    void writeData(DataWriter writer) throws IOException;
    void handleClientSide(ClientImpl client);
    void handleServerSide(Client client, ServerImpl server);
}