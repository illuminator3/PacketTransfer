package me.illuminator3.packettransfer.packet.pre;

import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.packet.PacketEncoder;

public class SimplePacketEncoder
    extends PacketEncoder
{
    private final String mid;

    public SimplePacketEncoder()
    {
        this("@");
    }

    public SimplePacketEncoder(String mid)
    {
        this.mid = mid;
    }

    @Override
    public String encode(DataWriter writer, int packetId)
    {
        return packetId + mid + writer.toString();
    }
}