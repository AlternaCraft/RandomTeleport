// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:04:26
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GenAleatorio.java

package jlh.minecraft.clases.randomTeleport;


public class GenAleatorio
{

    public GenAleatorio()
    {
    }

    public static int genAl(int max, int min)
    {
        return (int)((double)min + Math.random() * (double)((max - min) + 1));
    }

    public static String genNombre(int longitud)
    {
        String nombre = "";
        for(int i = 0; i < longitud; i++)
            nombre = (new StringBuilder()).append(nombre).append((char)genAl(90, 65)).toString();

        return nombre;
    }

    public static final int MAX = 90;
    public static final int MIN = 65;
}
