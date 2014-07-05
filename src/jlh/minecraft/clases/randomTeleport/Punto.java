package jlh.minecraft.clases.randomTeleport;

/**
 * 
 * @author Julián
 */
public class Punto
{
    private int x;
    private int y;
    private int z;
    
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
}
