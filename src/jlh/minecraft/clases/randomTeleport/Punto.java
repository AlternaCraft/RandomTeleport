// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:04:21
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Punto.java

package jlh.minecraft.clases.randomTeleport;


public class Punto
{

    public Punto(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Punto(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    private int x;
    private int y;
    private int z;
}
