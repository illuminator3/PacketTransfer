package me.illuminator3.packettransfer.packet.pre;

import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.packet.DecodedPacket;
import me.illuminator3.packettransfer.packet.PacketDecoder;
import me.illuminator3.packettransfer.packet.data.PacketDataReader;

public class SimplePacketDecoder
    extends PacketDecoder
{
    private final String mid;

    public SimplePacketDecoder()
    {
        this("@");
    }

    public SimplePacketDecoder(String mid)
    {
        this.mid = mid;
    }

    @Override
    public DecodedPacket decode(String data)
    {
        String[] s = data.split(mid, 2);
        String idVal = s[0];
        int id = Integer.parseInt(idVal);
        String d = s[1];
        DataReader reader = new PacketDataReader(d);

        return new DecodedPacket()
        {
            @Override
            public int getId()
            {
                return id;
            }

            @Override
            public DataReader getData()
            {
                return reader;
            }
        };
    }
}