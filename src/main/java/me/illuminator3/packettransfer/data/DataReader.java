package me.illuminator3.packettransfer.data;

public abstract class DataReader
{
    public abstract String readString();
    public abstract int readInt();
    public abstract long readLong();
    public abstract short readShort();
    public abstract byte read();
    public abstract String toString();
    public abstract void fromString(String s);
    public abstract DataWriter toDataWriter();
}