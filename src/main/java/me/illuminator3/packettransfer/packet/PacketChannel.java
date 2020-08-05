package me.illuminator3.packettransfer.packet;

import java.io.IOException;

public interface PacketChannel
{
    void write(Packet packet) throws IOException;
    void open() throws IOException;
    void close() throws IOException;
}