package me.illuminator3.packettransfer.server.impl;

import me.illuminator3.packettransfer.packet.PacketDecoder;
import me.illuminator3.packettransfer.packet.PacketEncoder;
import me.illuminator3.packettransfer.packet.PacketHandler;
import me.illuminator3.packettransfer.server.Client;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ServerImpl
{
    boolean disconnectMe(Client me);
    void disconnect() throws IOException;
    boolean isConnected();
    void setPacketHandler(PacketHandler handler);
    PacketHandler getPacketHandler();
    void connect() throws IOException;
    void onConnect(Consumer<Client> consumer);
    void addDisconnectHandler(Function<Client, Boolean> handler);
    int getPort();
    PacketEncoder getEncoder();
    void setEncoder(PacketEncoder encoder);
    PacketDecoder getDecoder();
    void setDecoder(PacketDecoder decoder);
    List<Client> getConnectedClients();
}