package me.illuminator3.packettransfer.data;

import java.io.Serializable;

public abstract class DataWriter
    implements Serializable
{
    public abstract void writeString(String s);
    public abstract void writeInt(int i);
    public abstract void writeLong(long l);
    public abstract void writeShort(short s);
    public abstract void write(byte b);
    public abstract String toString();
    public abstract void fromString(String s);
    public abstract DataReader toDataReader();
}