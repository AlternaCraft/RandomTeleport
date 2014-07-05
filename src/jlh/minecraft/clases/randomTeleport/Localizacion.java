// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:04:24
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Localizacion.java

package jlh.minecraft.clases.randomTeleport;


// Referenced classes of package jlh.minecraft.clases.randomTeleport:
//            Punto

public class Localizacion
{

    public Localizacion(Punto p1, Punto p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean compPunto(Punto p)
    {
        return (p.getX() >= p1.getX() && p.getX() <= p2.getX() || p.getX() >= p2.getX() && p.getX() <= p1.getX()) && (p.getY() >= p1.getY() && p.getY() <= p2.getY() || p.getY() >= p2.getY() && p.getY() <= p1.getY()) && (p.getZ() >= p1.getZ() && p.getZ() <= p2.getZ() || p.getZ() >= p2.getZ() && p.getZ() <= p1.getZ());
    }

    private final Punto p1;
    private final Punto p2;
}
