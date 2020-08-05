package me.illuminator3.packettransfer.utils.list;

import java.util.ArrayList;
import java.util.Arrays;

public class HList<T>
    extends ArrayList<T>
{
    private int nIndex = 0;

    public HList() {}

    public HList(T[] o)
    {
        super(Arrays.asList(o));
    }

    public synchronized T next()
    {
        return get(nIndex++);
    }

    public synchronized int index()
    {
        return nIndex;
    }

    @SafeVarargs
    public static <T> HList<T> asList(T... o)
    {
        return new HList<>(o);
    }
}