package me.illuminator3.packettransfer.packet;

import java.util.HashMap;
import java.util.Map;

public abstract class PacketHandler
{
    private final Map<Integer, Class<? extends Packet>> registeredPackets = new HashMap<>();

    public abstract void registerPackets();

    protected void registerPacket(Class<? extends Packet> packet, int id)
    {
        if (registeredPackets.containsKey(id))
            throw new IndexOutOfBoundsException("Id is already registered");

        this.registeredPackets.put(id, packet);
    }

    public Class<? extends Packet> getPacketById(int id)
    {
        return registeredPackets.get(id);
    }

    public int getId(Packet packet)
    {
        return this.getId(packet.getClass());
    }

    public int getId(Class<? extends Packet> packet)
    {
        final int[] id = { -1 };

        registeredPackets.forEach((i, clazz) -> {
            if (id[0] != -1) return;

            if (clazz.equals(packet))
                id[0] = i;
        });

        return id[0];
    }
}