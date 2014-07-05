package jlh.minecraft.events.randomTeleport;

import java.util.List;
import jlh.minecraft.clases.randomTeleport.*;
import jlh.minecraft.plugin.randomTeleport.ConfigCommandExecutor;
import jlh.minecraft.plugin.randomTeleport.RandomTeleport;
import static jlh.minecraft.plugin.randomTeleport.RandomTeleport.PLUGIN;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;

/**
 * 
 * @author Julián
 */
public class EventosJug
    implements Listener, EventExecutor
{
    public static final int MAX_X = 10000;
    public static final int MIN_X = -500;
    public static final int Y = 150;
    public static final int MAX_Z = 10000;
    public static final int MIN_Z = -500;
    private final RandomTeleport plugin;
    
    public EventosJug(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(e.getPlayer().getLocation().getWorld().getName().
                compareTo((String)plugin.config.get(0)) == 0)
        {
            Location player = e.getPlayer().getLocation();
            Punto p = new Punto((int)player.getX(), (int)player.getY(), (int)player.getZ());
            if(plugin.cubo.compPunto(p))
            {
                e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD + "Estás siendo teletransportado");
                Location llegada = new Location(Bukkit.getWorld((String)plugin.config.get(3)), 
                        GenAleatorio.genAl(MAX_X, MIN_X), Y, GenAleatorio.genAl(MAX_Z, MIN_Z));
                e.getPlayer().teleport(llegada);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        List lista = ConfigCommandExecutor.lista;
        for(int i = 0; i < lista.size(); i++)
        {
            if(e.getMessage().compareToIgnoreCase("marcar") == 0 || 
                    e.getMessage().compareToIgnoreCase("reiniciar") == 0 || 
                    e.getMessage().compareToIgnoreCase("finalizar") == 0)
            {
                if(((Configuracion)lista.get(i)).getJugador().equals(e.getPlayer()))
                {
                    Location l = e.getPlayer().getLocation();
                    plugin.getConfig().set("mundo", e.getPlayer().getWorld().getName());
                    plugin.saveConfig();
                    
                    if(!((Configuracion)lista.get(i)).isParte1())
                    {
                        plugin.getConfig().set("Pos1.x", (int)l.getX());
                        plugin.getConfig().set("Pos1.y", (int)l.getY());
                        plugin.getConfig().set("Pos1.z", (int)l.getZ());
                        plugin.saveConfig();
                        ((Configuracion)lista.get(i)).setParte1(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Posición actual añadida correctamente");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                                "ahora vaya a la siguiente posicion y ponga \"marcar\" nuevamente");
                        break;
                    }
                    if(!((Configuracion)lista.get(i)).isParte2())
                    {
                        plugin.getConfig().set("Pos2.x", Integer.valueOf((int)l.getX()));
                        plugin.getConfig().set("Pos2.y", Integer.valueOf((int)l.getY()));
                        plugin.getConfig().set("Pos2.z", Integer.valueOf((int)l.getZ()));
                        plugin.saveConfig();
                        ((Configuracion)lista.get(i)).setParte2(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Posición actual añadida correctamente, has creado la zona de teletransporte!!.");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                                "Ahora escriba el nombre donde quieres que los players sean teletransportados.");
                        break;
                    }
                    if(e.getMessage().compareToIgnoreCase("reiniciar") == 0)
                    {
                        ((Configuracion)lista.get(i)).desactivar();
                        i--;
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Has reiniciado los datos del plugin.");
                        e.setCancelled(true);
                    }
                    else
                    {
                        lista.remove(i);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD + 
                                "Has terminado de configurar el plugin.");
                        break;
                    }
                }
            }
            if(!((Configuracion)lista.get(i)).isParte2() || 
                    ((Configuracion)lista.get(i)).isParte3()) {
                
                if(plugin.getServer().getWorld(e.getMessage()) != null)
                {
                    plugin.getConfig().set("llegada", e.getMessage());
                    plugin.saveConfig();
                    ((Configuracion)lista.get(i)).setParte3(true);
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + "Mundo " 
                            + e.getMessage() + " añadido correctamente.");
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                            "Ahora puedes finalizar la configuracion poniendo finalizar.");
                } else
                {
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.RED + 
                            "El mundo " + e.getMessage() + " no existe");
                }
                
                e.setCancelled(true);
                break;
            }
        }
        e.setCancelled(true);
    }

    @Override
    public void execute(Listener ll, Event event)
        throws EventException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
