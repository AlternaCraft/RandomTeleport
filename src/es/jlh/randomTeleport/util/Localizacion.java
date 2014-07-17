/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.jlh.randomTeleport.util;

/**
 *
 * @author Julian
 */
public class Localizacion {
    
    private String zona;
    private Punto p1;
    private Punto p2;
    private String origen;
    private String llegada;
    private int no_pvp; 

    public Localizacion() {};
    
    public Localizacion(String zona, Punto p1, Punto p2, String origen, String llegada, int no_pvp) {
        this.zona = zona;
        this.p1 = p1;
        this.p2 = p2;
        this.origen = origen;
        this.llegada = llegada;
        this.no_pvp = no_pvp;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Punto getPunto1() {
        return p1;
    }

    public void setPunto1(Punto p1) {
        this.p1 = p1;
    }

    public Punto getPunto2() {
        return p2;
    }

    public void setPunto2(Punto p2) {
        this.p2 = p2;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public int getNo_pvp() {
        return no_pvp;
    }

    public void setNo_pvp(int no_pvp) {
        this.no_pvp = no_pvp;
    }
    
    /**
     * Metodo para comprobar si un jugador se encuentra en una localizacion
     *
     * @param p Coordenadas del jugador
     * @return true si se encuentra y false en caso de que no se encuentre
     */
    public boolean compPunto(Punto p) {
        if (p.getX() >= p1.getX() && p.getX() <= p2.getX()
                || p.getX() >= p2.getX() && p.getX() <= p1.getX()) {
            if (p.getY() >= p1.getY() && p.getY() <= p2.getY()
                    || p.getY() >= p2.getY() && p.getY() <= p1.getY()) {
                if (p.getZ() >= p1.getZ() && p.getZ() <= p2.getZ()
                        || p.getZ() >= p2.getZ() && p.getZ() <= p1.getZ()) {
                    return true;
                }
            }
        }
        return false;
    }
}
