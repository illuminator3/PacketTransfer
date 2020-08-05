package me.illuminator3.packettransfer.packet.pre;

import me.illuminator3.packettransfer.packet.Packet;
import me.illuminator3.packettransfer.packet.PacketHandler;

public class PrePacketHandler
    extends PacketHandler
{
    @Override public void registerPackets() {}

    public void addPacket(int id, Packet packet)
    {
        this.addPacket(id, packet.getClass());
    }

    public void addPacket(int id, Class<? extends Packet> packet)
    {
        this.registerPacket(packet, id);
    }
}