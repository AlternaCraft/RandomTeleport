package es.jlh.randomTeleport.handlers;

import es.jlh.randomTeleport.util.Lang;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.GenAleatorio;
import es.jlh.randomTeleport.util.Localizacion;
import es.jlh.randomTeleport.util.PlayerZona;
import es.jlh.randomTeleport.util.Punto;
import es.jlh.randomTeleport.util.Zona;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import static org.bukkit.entity.EntityType.PLAYER;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Julián
 */
public class HandleTeleport implements Listener, EventExecutor {

    public static final int MAX_X = 10000;
    public static final int MIN_X = -5000;
    public static final int Y = 125;
    public static final int MAX_Z = 10000;
    public static final int MIN_Z = -5000;
    
    public static final int TICKS = 20;
    
    public static final int X_CHUNK = 15;
    public static final int Y_CHUNK = 65;// Max 127 (Rendimiento)
    public static final int Z_CHUNK = 15;

    private final RandomTeleport plugin;
    private final List<PlayerZona> players = new ArrayList();
    private final List<Player> nopvp = new ArrayList();

    public HandleTeleport(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    // Evento no detecta el player en caida
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        List<Localizacion> zonas = plugin.sm.getConfiguration();
        
        Player pl = e.getPlayer();
        World destino = null;
        Location mundoLlegada = null;
        
        for (int i = 0; i < zonas.size(); i++) {
            Localizacion zona = zonas.get(i);
                        
            if (pl.getLocation().getWorld().getName().compareTo(zona.getOrigen()) == 0) {
                Location player = pl.getLocation();
                Punto p = new Punto((int) player.getX(), (int) player.getY(), (int) player.getZ());

                try {
                    destino = Bukkit.getServer().getWorld(zona.getLlegada());

                    if (zona.compPunto(p)) {
                        if (zona.getSzonas().isEmpty()) {
                            boolean resul = false;
                            do {
                                mundoLlegada = new Location(destino, GenAleatorio.
                                        genAl(MAX_X, MIN_X), Y, GenAleatorio.genAl(MAX_Z, MIN_Z));                    
                                resul = compruebaLoc(mundoLlegada);
                            }
                            while (!resul);
                        }
                        else {
                            do { // Posible bucle infinito
                                int al = GenAleatorio.genAl(zona.getSzonas().size()-1, 0);
                                Zona pr = zona.getSzonas().get(al);
                                if (pr.compLoc(destino)) {
                                    mundoLlegada = pr.locAl(destino);
                                    break;
                                }
                            }
                            while (true);
                        }

                        pl.teleport(mundoLlegada);  
                        pl.setGameMode(GameMode.SURVIVAL);
                        pl.sendMessage(PLUGIN + Lang.PLAYER_TELEPORTED.getText());                        
                        players.add(new PlayerZona(zona.getZona(),pl));
                    }
                } 
                catch (Exception ex) {
                    pl.sendMessage(PLUGIN + Lang.PLUGIN_ERROR_TP.getText());
                }
            }
        }        
    }
    
    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getEntity();
        for (PlayerZona pz : players) {
            if (pz.getJ().getName().equals(p.getName())) {                
                if (e.getCause() == DamageCause.FALL) {                    
                    e.setCancelled(true);
                    pz.setJ(p);
                    aplicaExtras(pz);
                    break;
                }
            } 
        }      
        
        if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)e;
            Entity damager = ev.getDamager();
            if (damager instanceof Player) {
                Player pvp = (Player)damager;
                for (Player player : nopvp) {
                    if (pvp.getName().equals(player.getName())) {
                        pvp.sendMessage(PLUGIN + Lang.PLAYER_NO_PVP.getText());
                        e.setCancelled(true);
                        break;
                    }
                }                               
            }
        }
    }
    
    public void aplicaExtras(final PlayerZona pz) {
        int seg = plugin.getConfig().getInt(pz.getZona() + ".tiempo.no_pvp");

        final Player pl = pz.getJ();
        
        pl.setNoDamageTicks(TICKS * seg);
        pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TICKS * seg, 5));
        pl.sendMessage(PLUGIN + Lang.PLAYER_INVULNERABILITY.
                getText().replaceAll("%TIME%", String.valueOf(seg)));
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().getPlayer(pl.getUniqueId()).removePotionEffect(PotionEffectType.SPEED);                        
                Bukkit.getServer().getPlayer(pl.getUniqueId()).sendMessage(PLUGIN + Lang.PLAYER_SI_PVP.getText());
                nopvp.remove(pl);
            }
        }, TICKS * seg);
        
        nopvp.add(pl);
        players.remove(pz); // Lo elimino de ls lista
    }
    
    public boolean compruebaLoc(Location loc) {        
        Entity[] lista = loc.getChunk().getEntities();
        
        // Comprueba el chunk en busca de otros jugadores
        for (Entity lista1 : lista) {
            if (lista1.getType() == PLAYER) {
                return false;
            }
        }
        
        // Comprueba el chunk en busca de liquidos
        for (int i = 0; i < X_CHUNK; i++) {
            for (int j = 0; j < Y_CHUNK; j++) {
                for (int k = 0; k < Z_CHUNK; k++) {
                    if (loc.getChunk().getBlock(i, j, k).isLiquid()) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void execute(Listener ll, Event event)
            throws EventException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
