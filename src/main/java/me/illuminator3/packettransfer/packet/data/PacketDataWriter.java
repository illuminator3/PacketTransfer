package me.illuminator3.packettransfer.packet.data;

import me.illuminator3.packettransfer.data.DataReader;
import me.illuminator3.packettransfer.data.DataWriter;
import me.illuminator3.packettransfer.utils.list.HList;

import java.util.ArrayList;
import java.util.List;

public class PacketDataWriter
    extends DataWriter
{
    private final DataWriter $THIS = this;

    private HList<String> strings = new HList<>();
    private HList<Integer> ints = new HList<>();
    private HList<Long> longs = new HList<>();
    private HList<Short> shorts = new HList<>();
    private HList<Byte> bytes = new HList<>();

    public PacketDataWriter()
    {

    }

    public PacketDataWriter(String s)
    {
        fromString(s);
    }

    public PacketDataWriter(HList<String> strings, HList<Integer> ints, HList<Long> longs, HList<Short> shorts, HList<Byte> bytes)
    {
        this.strings = strings;
        this.ints = ints;
        this.longs = longs;
        this.shorts = shorts;
        this.bytes = bytes;
    }

    @Override
    public void writeString(String s)
    {
        strings.add(s);
    }

    @Override
    public void writeInt(int i)
    {
        ints.add(i);
    }

    @Override
    public void writeLong(long l)
    {
        longs.add(l);
    }

    @Override
    public void writeShort(short s)
    {
        shorts.add(s);
    }

    @Override
    public void write(byte b)
    {
        bytes.add(b);
    }

    @Override
    public String toString()
    {
        List<List<?>> lists = new ArrayList<>();

        lists.add(strings);
        lists.add(ints);
        lists.add(longs);
        lists.add(shorts);
        lists.add(bytes);

        return lists.toString();
    }

    @Override
    public void fromString(String s)
    {
        s = s.substring(1, s.length() - 1);

        String[] split = s.split(", ");

        String strings_ = split[0],
               ints_ = split[1],
               longs_ = split[2],
               shorts_ = split[3],
               bytes_ = split[4];

        String[] strings__ = strings_.substring(1, strings_.length() - 1).split(", "),
                 ints__ = ints_.substring(1, ints_.length() - 1).split(", "),
                 longs__ = longs_.substring(1, longs_.length() - 1).split(", "),
                 shorts__ = shorts_.substring(1, shorts_.length() - 1).split(", "),
                 bytes__ = bytes_.substring(1, bytes_.length() - 1).split(", ");

        HList<String> _strings__ = new HList<>();
        HList<Integer> _ints__ = new HList<>();
        HList<Long> _longs__ = new HList<>();
        HList<Short> _shorts__ = new HList<>();
        HList<Byte> _bytes__ = new HList<>();

        for (String str : strings__)
        {
            if (str == null || str.trim().isEmpty()) continue;

            _strings__.add(str);
        }

        for (String s1 : ints__)
        {
            if (s1 == null || s1.trim().isEmpty()) continue;

            _ints__.add(Integer.valueOf(s1));
        }

        for (String s2 : longs__)
        {
            if (s2 == null || s2.trim().isEmpty()) continue;

            _longs__.add(Long.valueOf(s2));
        }

        for (String s3 : shorts__)
        {
            if (s3 == null || s3.trim().isEmpty()) continue;

            _shorts__.add(Short.valueOf(s3));
        }

        for (String s4 : bytes__)
        {
            if (s4 == null || s4.trim().isEmpty()) continue;

            _bytes__.add(Byte.valueOf(s4));
        }

        strings = _strings__;
        ints = _ints__;
        longs = _longs__;
        shorts = _shorts__;
        bytes = _bytes__;
    }

    @Override
    public DataReader toDataReader()
    {
        return new DataReader()
        {
            @Override
            public String readString()
            {
                return strings.next();
            }

            @Override
            public int readInt()
            {
                return ints.next();
            }

            @Override
            public long readLong()
            {
                return longs.next();
            }

            @Override
            public short readShort()
            {
                return shorts.next();
            }

            @Override
            public byte read()
            {
                return bytes.next();
            }

            @Override
            public String toString()
            {
                return $THIS.toString();
            }

            @Override
            public void fromString(String s)
            {
                $THIS.fromString(s);
            }

            @Override
            public DataWriter toDataWriter()
            {
                return $THIS;
            }
        };
    }
}