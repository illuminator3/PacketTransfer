package me.illuminator3.packettransfer.packet.pre;

import me.illuminator3.packettransfer.packet.Packet;
import me.illuminator3.packettransfer.packet.PacketHandler;

import java.util.HashMap;
import java.util.Map;

public class PrePacketHandler
    extends PacketHandler
{
    private final Map<Integer, Class<? extends Packet>> queue = new HashMap<>();

    @Override
    public void registerPackets()
    {
        this.queue.forEach(this::addPacket);
    }

    public void addPacket(int id, Packet packet)
    {
        this.addPacket(id, packet.getClass());
    }

    public void addPacket(int id, Class<? extends Packet> packet)
    {
        this.registerPacket(packet, id);
    }

    public void queuePacket(int id, Packet packet)
    {
        this.queuePacket(id, packet.getClass());
    }

    public void queuePacket(int id, Class<? extends Packet> packet)
    {
        this.queue.put(id, packet);
    }
}