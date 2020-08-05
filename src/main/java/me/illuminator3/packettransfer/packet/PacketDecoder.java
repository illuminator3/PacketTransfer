package me.illuminator3.packettransfer.packet;

public abstract class PacketDecoder
{
    public abstract DecodedPacket decode(String data);
}