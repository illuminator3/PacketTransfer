package me.illuminator3.packettransfer.packet;

import me.illuminator3.packettransfer.data.DataWriter;

public abstract class PacketEncoder
{
    public abstract String encode(DataWriter writer, int packetId);
}