package jlh.minecraft.clases.randomTeleport;

/**
 * 
 * @author Julián
 */
public class Localizacion
{
    private final Punto p1;
    private final Punto p2;
    
    public Localizacion(Punto p1, Punto p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    /**
     * Metodo para comprobar si un jugador se encuentra en una localizacion
     * @param p Coordenadas del jugador
     * @return true si se encuentra y false en caso de que no se encuentre
     */
    public boolean compPunto(Punto p)
    {
        return (p.getX() >= p1.getX() && p.getX() <= p2.getX() || 
                p.getX() >= p2.getX() && p.getX() <= p1.getX()) && 
                (p.getY() >= p1.getY() && p.getY() <= p2.getY() || 
                p.getY() >= p2.getY() && p.getY() <= p1.getY()) && 
                (p.getZ() >= p1.getZ() && p.getZ() <= p2.getZ() || 
                p.getZ() >= p2.getZ() && p.getZ() <= p1.getZ());
    }
}
