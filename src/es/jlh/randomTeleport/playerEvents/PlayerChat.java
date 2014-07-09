package es.jlh.randomTeleport.playerEvents;

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
    private Punto mundo;
    private Punto mundo2;
    private String origen;
    private String llegada;
    private int no_pvp;

    public PlayerChat(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        List lista = ConfigCommandExecutor.lista;

        for (int i = 0; i < lista.size(); i++) {
            if (e.getMessage().compareToIgnoreCase("marcar") == 0
                    || e.getMessage().compareToIgnoreCase("reiniciar") == 0
                    || e.getMessage().compareToIgnoreCase("finalizar") == 0) {
                if (((Configuracion) lista.get(i)).getJugador().equals(e.getPlayer())) {
                    Location l = e.getPlayer().getLocation();
                    if (!((Configuracion) lista.get(i)).isParte1()
                            && e.getMessage().compareToIgnoreCase("marcar") == 0) {
                        origen = e.getPlayer().getWorld().getName();
                        mundo = new Punto((int) l.getX(), (int) l.getY(), (int) l.getZ());
                        ((Configuracion) lista.get(i)).setParte1(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                                + "Posición actual añadida correctamente");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                                + "ahora vaya a la siguiente posicion y ponga \"marcar\" nuevamente");
                    } else if (!((Configuracion) lista.get(i)).isParte2()
                            && e.getMessage().compareToIgnoreCase("marcar") == 0) {
                        mundo2 = new Punto((int) l.getX(), (int) l.getY(), (int) l.getZ());
                        ((Configuracion) lista.get(i)).setParte2(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                                + "Posición actual añadida correctamente, "
                                + "has creado la zona de teletransporte!!.");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                                + "Ahora escriba el nombre donde quieres que "
                                + "los players sean teletransportados.");
                    } else if (e.getMessage().compareToIgnoreCase("reiniciar") == 0) {
                        ((Configuracion) lista.get(i)).desactivar();
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN
                                + "Has reiniciado los datos del plugin.");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                                + "Ahora vaya a una posicion y escriba \"marcar\" "
                                + "para marcar esa posicion");
                    } else if (e.getMessage().compareToIgnoreCase("finalizar") == 0
                            && !((Configuracion) lista.get(i)).isParte4()) {
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                                + "Antes de finalizar tienes que terminar de "
                                + "configurarlo!!, recuerda que puedes usar "
                                + "\"reiniciar\" por si te equivocaste.");
                    } else if (e.getMessage().compareToIgnoreCase("finalizar") == 0) {
                        plugin.getConfig().set("origen.alias", origen);
                        plugin.getConfig().set("origen.pos1.x", mundo.getX());
                        plugin.getConfig().set("origen.pos1.y", mundo.getY());
                        plugin.getConfig().set("origen.pos1.z", mundo.getZ());
                        plugin.getConfig().set("origen.pos2.x", mundo2.getX());
                        plugin.getConfig().set("origen.pos2.y", mundo2.getY());
                        plugin.getConfig().set("origen.pos2.z", mundo2.getZ());
                        plugin.getConfig().set("destino.alias", llegada);
                        plugin.getConfig().set("tiempo.no_pvp", no_pvp);
                        plugin.saveConfig();
                        lista.remove(i);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD
                                + "Has terminado de configurar el plugin. Ahora haz"
                                + " \"/rtreload\" para cargar la nueva configuración");
                    } else {
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.RED + "Deja "
                                + "de intentar marcar, ya has terminado la zona!!!");
                    }
                    // Cerrar
                    e.setCancelled(true);
                    break;
                }
            } 
            else if (((Configuracion) lista.get(i)).isParte2() && 
                    !((Configuracion) lista.get(i)).isParte3() &&
                    ((Configuracion) lista.get(i)).getJugador().equals(e.getPlayer())) {

                if (plugin.getServer().getWorld(e.getMessage()) != null) {
                    llegada = e.getMessage();
                    ((Configuracion) lista.get(i)).setParte3(true);
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + "Mundo "
                            + e.getMessage() + " añadido correctamente.");
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                            + "Ahora escribe los segundos de proteccion PvP.");
                } else {
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                            + "El mundo " + e.getMessage() + " no existe");
                }

                e.setCancelled(true);
                break;
            } 
            else if (((Configuracion) lista.get(i)).isParte3() && 
                    ((Configuracion) lista.get(i)).getJugador().equals(e.getPlayer())) {

                try {
                    no_pvp = Integer.valueOf(e.getMessage());
                    ((Configuracion) lista.get(i)).setParte4(true);
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                            e.getMessage() + " segundos añadidos correctamente");
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE
                            + "Ahora puedes finalizar la configuracion poniendo \"finalizar\".");                    
                }
                catch (NumberFormatException ex) {
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.RED
                            + "Tienes que indicar un valor de segundos valido...");
                }
                
                e.setCancelled(true);
                break;
            }
        }
    }

    @Override
    public void execute(Listener ll, Event event)
            throws EventException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
