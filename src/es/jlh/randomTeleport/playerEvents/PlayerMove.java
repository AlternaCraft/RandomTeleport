package es.jlh.randomTeleport.playerEvents;

import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.DESTINO;
import static es.jlh.randomTeleport.plugin.RandomTeleport.ORIGEN;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import static es.jlh.randomTeleport.plugin.RandomTeleport.TIME_NO_PVP;
import es.jlh.randomTeleport.util.GenAleatorio;
import es.jlh.randomTeleport.util.Punto;
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

/**
 *
 * @author Julián
 */
public class PlayerMove implements Listener, EventExecutor {

    public static final int MAX_X = 10000;
    public static final int MIN_X = -5000;
    public static final int Y = 150;
    public static final int MAX_Z = 10000;
    public static final int MIN_Z = -5000;
    
    public static final int TICKS = 20;
    
    public static final int X_CHUNK = 15;
    public static final int Y_CHUNK = 65;// Max 127 (Rendimiento)
    public static final int Z_CHUNK = 15;

    private final RandomTeleport plugin;
    private List players = new ArrayList();

    public PlayerMove(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player pl = e.getPlayer();
        World destino = null;
        Location mundoLlegada = null;
        Punto p = null;
        boolean resul = false;
        
        if (pl.getLocation().getWorld().getName().compareTo((String) plugin.sm.getConfiguration().get(ORIGEN)) == 0) {
            Location player = pl.getLocation();
            p = new Punto((int) player.getX(), (int) player.getY(), (int) player.getZ());

            try {
                destino = Bukkit.getServer().getWorld((String) plugin.sm.getConfiguration().get(DESTINO));

                if (plugin.sm.getCubo().compPunto(p)) {
                    pl.sendMessage(PLUGIN + ChatColor.GOLD + Lang.PLAYER_TELEPORTED.getText());
                    
                    do {
                        mundoLlegada = new Location(destino, GenAleatorio.
                                genAl(MAX_X, MIN_X), Y, GenAleatorio.genAl(MAX_Z, MIN_Z));                    
                        resul = compruebaLoc(mundoLlegada);
                    }
                    while (!resul);
                    
                    pl.teleport(mundoLlegada);                    
                    players.add(pl);
                }
            } catch (Exception ex) {
                pl.sendMessage(PLUGIN + ChatColor.RED
                        + "El mundo " + destino.getName() + " no existe");
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getEntity();
        if (players.contains(p)) {
            if (e.getCause() == DamageCause.FALL) {
                aplicaExtras(p);
                e.setCancelled(true);
            }
        }
        
        if (e instanceof EntityDamageByEntityEvent) {
            for (Object player : players) {
                EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)e;
                Entity damager = ev.getDamager();
                if (damager instanceof Player) {
                    p = (Player)damager;
                    Player pvp = Bukkit.getServer().getPlayer(p.getName());
                    if (pvp.equals(player)) {
                        pvp.sendMessage(PLUGIN + ChatColor.RED + 
                                Lang.PLAYER_NO_PVP.getText());
                        e.setCancelled(true);
                        break;
                    }
                }               
            }
        }
    }
    
    public void aplicaExtras(Player pl) {
        int seg = (int) plugin.sm.getConfiguration().get(TIME_NO_PVP);

        pl.setNoDamageTicks(TICKS * seg);
        pl.sendMessage(PLUGIN + ChatColor.BLUE + Lang.PLAYER_INVULNERABILITY.
                getText().replaceAll("%TIME%", String.valueOf(seg)));
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                        Bukkit.getServer().broadcastMessage(PLUGIN + 
                                ChatColor.BLUE + Lang.PLAYER_SI_PVP.getText());
                }
        }, TICKS * seg);        
        
        players.remove(pl);
    }
    
    public boolean compruebaLoc(Location loc) {        
        Entity[] lista = loc.getChunk().getEntities();
        
        for (int i = 0; i < lista.length; i++) {
            if (lista[i].getType() == PLAYER) {
                return false;
            }
        }
        
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
