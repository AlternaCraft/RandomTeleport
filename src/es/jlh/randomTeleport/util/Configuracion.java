package es.jlh.randomTeleport.util;

import org.bukkit.entity.Player;

public class Configuracion {

    private final Player jugador;
    
    private boolean parte1; // Gestion de punto 1
    private boolean parte2; // Gestion de punto 2
    private boolean parte3; // Gestion de destino
    private boolean parte4; // Gestion de segundos no-pvp
    private boolean parte5; // Quiere subzona? y para salir
    private boolean parte6; // Gestion de punto 1 y 2 subzona
    
    private Localizacion l = new Localizacion();

    public Configuracion(Player j, boolean p1, boolean p2, boolean p3, boolean p4, 
            boolean p5, boolean p6) {
        this.jugador = j;
        this.parte1 = p1;
        this.parte2 = p2;
        this.parte3 = p3;
        this.parte4 = p4;
        this.parte5 = p5;
        this.parte6 = p6;
    }

    public Player getJugador() {
        return jugador;
    }

    public boolean isParte1() {
        return parte1;
    }

    public boolean isParte2() {
        return parte2;
    }

    public boolean isParte3() {
        return parte3;
    }

    public boolean isParte4() {
        return parte4;
    }

    public boolean isParte5() {
        return parte5;
    }

    public boolean isParte6() {
        return parte6;
    }

    public void setParte1(boolean parte1) {
        this.parte1 = parte1;
    }

    public void setParte2(boolean parte2) {
        this.parte2 = parte2;
    }

    public void setParte3(boolean parte3) {
        this.parte3 = parte3;
    }

    public void setParte4(boolean parte4) {
        this.parte4 = parte4;
    }

    public void setParte5(boolean parte5) {
        this.parte5 = parte5;
    }

    public void setParte6(boolean parte6) {
        this.parte6 = parte6;
    }
    
    public Localizacion getL() {
        return l;
    }

    public void reiniciar() {
        parte1 = false;
        parte2 = false;
        parte3 = false;
        parte4 = false;
        parte5 = false;
        parte6 = false;
    }
}
