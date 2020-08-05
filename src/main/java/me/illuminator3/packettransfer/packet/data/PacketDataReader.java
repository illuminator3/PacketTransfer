package me.illuminator3.packettransfer.packet.data;

import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.utils.list.HList;

public class PacketDataReader
    extends DataReader
{
    private DataReader prev;
    private HList<String> strings = new HList<>();
    private HList<Integer> ints = new HList<>();
    private HList<Long> longs = new HList<>();
    private HList<Short> shorts = new HList<>();
    private HList<Byte> bytes = new HList<>();

    public PacketDataReader()
    {

    }

    public PacketDataReader(HList<String> strings, HList<Integer> ints, HList<Long> longs, HList<Short> shorts, HList<Byte> bytes)
    {
        this.strings = strings;
        this.ints = ints;
        this.longs = longs;
        this.shorts = shorts;
        this.bytes = bytes;
    }


    @Override
    public String readString()
    {
        return prev != null ? prev.readString() : strings.next();
    }

    @Override
    public int readInt()
    {
        return prev != null ? prev.readInt() : ints.next();
    }

    @Override
    public long readLong()
    {
        return prev != null ? prev.readLong() : longs.next();
    }

    @Override
    public short readShort()
    {
        return prev != null ? prev.readShort() : shorts.next();
    }

    @Override
    public byte read()
    {
        return prev != null ? prev.read() : bytes.next();
    }

    @Override
    public String toString()
    {
        return toDataWriter().toString();
    }

    @Override
    public void fromString(String s)
    {
        PacketDataWriter writer = new PacketDataWriter();

        writer.fromString(s);

        this.prev = writer.toDataReader();
    }

    @Override
    public DataWriter toDataWriter()
    {
        return new PacketDataWriter(this.strings, this.ints, this.longs, this.shorts, this.bytes);
    }
}