package me.illuminator3.packettransfer.client.impl;

import me.illuminator3.packettransfer.packet.PacketDecoder;
import me.illuminator3.packettransfer.packet.PacketEncoder;
import me.illuminator3.packettransfer.packet.PacketHandler;

import java.io.IOException;

public interface ClientImpl
{
    void disconnect() throws IOException;
    boolean isConnected();
    Connection getConnection();
    void setPacketHandler(PacketHandler handler);
    PacketHandler getPacketHandler();
    void connect() throws IOException;
    int getPort();
    String getHost();
    PacketEncoder getEncoder();
    void setEncoder(PacketEncoder encoder);
    PacketDecoder getDecoder();
    void setDecoder(PacketDecoder decoder);
}