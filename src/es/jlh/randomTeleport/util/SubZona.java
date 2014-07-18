/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.jlh.randomTeleport.util;

import es.jlh.randomTeleport.events.PlayerTeleport;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Julian
 */
public class SubZona {
    
    private Punto p1;
    private Punto p2;

    public SubZona(Punto p1, Punto p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Punto getP1() {
        return p1;
    }

    public void setP1(Punto p1) {
        this.p1 = p1;
    }

    public Punto getP2() {
        return p2;
    }

    public void setP2(Punto p2) {
        this.p2 = p2;
    }
    
    public Location locAl(World mundo) {
        int pos1, pos2, pos3;     
        
        if (p1.getX()>p2.getX()) {
            pos1 = GenAleatorio.genAl(p1.getX(), p2.getX());
        }
        else if (p1.getX()==p2.getX()) {
            pos1 = p1.getX();
        }
        else {
            pos1 = GenAleatorio.genAl(p2.getX(), p1.getX());
        }
        
        pos2 = PlayerTeleport.Y;
        
        if (p1.getZ()>p2.getZ()) {
            pos3 = GenAleatorio.genAl(p1.getZ(), p2.getZ());
        }
        else if (p1.getZ()==p2.getZ()) {
            pos3 = p1.getZ();
        }        
        else {
            pos3 = GenAleatorio.genAl(p2.getZ(), p1.getZ());
        }
        
        return new Location(mundo, pos1, pos2, pos3);
    }
    
    public boolean compLoc(World mundo) {
        int x,z;
        int minx,minz;
        
        if (p1.getX()>=p2.getX()) {
            x = p1.getX();
            minx = p2.getX();
        }
        else {
            x = p2.getX();
            minx = p1.getX();
        }
        if (p1.getZ()>=p2.getZ()) {
            z = p1.getZ();
            minz = p2.getZ();
        }
        else {
            z = p2.getZ();
            minz = p1.getZ();
        }

        for (int i = minx; i <= x; i++) {
            for (int j = 0; j <= 65; j++) {
                for (int k = minz; k <= z; k++) {
                    Location l = new Location(mundo, i, j, k);
                    if (l.getBlock().isLiquid()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
