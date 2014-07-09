package es.jlh.randomTeleport.util;

import org.bukkit.entity.Player;

public class Configuracion {

    private final Player jugador;
    private boolean parte1;
    private boolean parte2;
    private boolean parte3;
    private boolean parte4;

    public Configuracion(Player j, boolean p1, boolean p2, boolean p3, boolean p4) {
        jugador = j;
        parte1 = p1;
        parte2 = p2;
        parte3 = p3;
        parte4 = p4;
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

    public void desactivar() {
        parte1 = false;
        parte2 = false;
        parte3 = false;
        parte4 = false;
    }
}
