package es.jlh.randomTeleport.events;

import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Configuracion;
import es.jlh.randomTeleport.util.Punto;
import java.util.List;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

/**
 *
 * @author Julián
 */
public class PlayerChat implements Listener, EventExecutor {

    private final RandomTeleport plugin;

    public PlayerChat(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        List<Configuracion> lista = ConfigCommandExecutor.lista;

        for (int i = 0; i < lista.size(); i++) {
            
            Configuracion adm = lista.get(i);
            
            if (e.getMessage().compareToIgnoreCase("marcar") == 0) {
                marcas(adm, e, lista, i);
                e.setCancelled(true);
                break;
            }
            else if (e.getMessage().compareToIgnoreCase("reiniciar") == 0) {                      
                adm.desactivar();
                e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                        + "Has reiniciado los datos del plugin.");
                e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                        + "Ahora vaya a una posicion y escriba \"marcar\" "
                        + "para marcar esa posicion");
                e.setCancelled(true);
                break;
            }
            else if (e.getMessage().compareToIgnoreCase("cancelar") == 0) {
                lista.remove(adm);
                e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD + "La "
                        + "configuracion ha finalizado sin cambios");      
                e.setCancelled(true);
                break;
            }
            else if (adm.isParte2() && !adm.isParte3() && adm.getJugador().equals(e.getPlayer())) {
                world(adm, e);
                e.setCancelled(true);
                break;
            } 
            else if (adm.isParte3() && adm.getJugador().equals(e.getPlayer())) {
                time(adm, e);
                e.setCancelled(true);
                break;
            }
            else if (e.getMessage().compareToIgnoreCase("finalizar") == 0) {
                finalizar(adm, e, lista, i);
                e.setCancelled(true);
                break;
            }            
        }
    } 

    public void marcas(Configuracion adm, AsyncPlayerChatEvent e, 
            List<Configuracion> lista, int i) {
        
        Location l = e.getPlayer().getLocation();
        
        if (!lista.get(i).isParte1()) {                        
            adm.getL().setOrigen(e.getPlayer().getWorld().getName());
            adm.getL().setPunto1(new Punto((int) l.getX(), (int) l.getY(), (int) l.getZ()));
            adm.setParte1(true);

            e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                    + "Posición actual añadida correctamente");
            e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                    + "ahora vaya a la siguiente posicion y ponga \"marcar\" nuevamente");
        } 
        else if (!lista.get(i).isParte2()) {                        
            adm.getL().setPunto2(new Punto((int) l.getX(), (int) l.getY(), (int) l.getZ()));
            adm.setParte2(true);

            e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                    + "Posición actual " + ChatColor.GOLD + "(" + 
                    ChatColor.LIGHT_PURPLE + (int)l.getX()+","+(int)l.getY()+","+(int)l.getZ()
                    + ChatColor.GOLD + ")" + ChatColor.GREEN + " añadida correctamente, "
                    + "has creado la zona de teletransporte!!.");
            e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                    + "Ahora escriba el nombre donde quieres que "
                    + "los players sean teletransportados.");
        }
        else {
            e.getPlayer().sendMessage(PLUGIN + ChatColor.RED + "Deja "
                    + "de intentar marcar, ya has terminado la zona!!!");
        }
    }
    
    public void world(Configuracion adm, AsyncPlayerChatEvent e) {
        
        if (plugin.getServer().getWorld(e.getMessage()) != null) {
            adm.getL().setLlegada(e.getMessage());
            adm.setParte3(true);

            e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + "Mundo "
                    + e.getMessage() + " añadido correctamente.");
            e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                    + "Ahora escribe los segundos de proteccion PvP.");
        } 
        else {
            e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                    + "El mundo " + e.getMessage() + " no existe");
        }
    }
    
    public void time(Configuracion adm, AsyncPlayerChatEvent e) {
        try {
            adm.getL().setNo_pvp(Integer.valueOf(e.getMessage()));
            adm.setParte4(true);

            e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                    e.getMessage() + " segundos añadidos correctamente");
            e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                    + "Ahora puedes finalizar la configuracion poniendo \"finalizar\".");                    
        }
        catch (NumberFormatException ex) {
            e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                    + "Tienes que indicar un valor de segundos valido...");
        }
    }
    
    public void finalizar(Configuracion adm, AsyncPlayerChatEvent e, 
            List<Configuracion> lista, int i) {
        
        if (!adm.isParte4()) {
            e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                    + "Antes de finalizar tienes que terminar de "
                    + "configurarlo!!, recuerda que puedes usar "
                    + "\"reiniciar\" por si te equivocaste.");
        } 
        else {
            List<String> zonas = (List<String>)plugin.getConfig().getList("zonasActivas");
            if (!zonas.contains(adm.getL().getZona())) {
                zonas.add(adm.getL().getZona());
            }
            plugin.getConfig().set(adm.getL().getZona() + ".origen.alias", adm.getL().getOrigen());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos1.x", adm.getL().getPunto1().getX());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos1.y", adm.getL().getPunto1().getY());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos1.z", adm.getL().getPunto1().getZ());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos2.x", adm.getL().getPunto2().getX());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos2.y", adm.getL().getPunto2().getY());
            plugin.getConfig().set(adm.getL().getZona() + ".origen.pos2.z", adm.getL().getPunto2().getZ());
            plugin.getConfig().set(adm.getL().getZona() + ".destino.alias", adm.getL().getLlegada());
            plugin.getConfig().set(adm.getL().getZona() + ".tiempo.no_pvp", adm.getL().getNo_pvp());
            plugin.saveConfig();

            lista.remove(i);

            e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD
                    + "Has terminado de configurar el plugin. Ahora haz"
                    + " \"/rtreload\" para cargar la nueva configuración");
        }
    }
       
    @Override
    public void execute(Listener ll, Event event)
            throws EventException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
