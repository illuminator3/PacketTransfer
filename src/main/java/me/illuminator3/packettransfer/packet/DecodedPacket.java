package me.illuminator3.packettransfer.packet;

import me.illuminator3.packettransfer.data.DataReader;

public abstract class DecodedPacket
{
    public abstract int getId();
    public abstract DataReader getData();
}