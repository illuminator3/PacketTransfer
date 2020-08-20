package me.illuminator3.packettransfer.client.impl;

import me.illuminator3.packettransfer.packet.PacketChannel;

import java.io.IOException;

public abstract class Connection
{
    public PacketChannel channel;

    public abstract void connect() throws IOException;
    public abstract void close() throws IOException;
    public abstract boolean isOpen();
}