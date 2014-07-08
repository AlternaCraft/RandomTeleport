package es.jlh.randomTeleport.events;

import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Configuracion;
import es.jlh.randomTeleport.util.GenAleatorio;
import es.jlh.randomTeleport.util.Punto;
import java.util.List;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
    public static final int Y = 100;
    public static final int MAX_Z = 10000;
    public static final int MIN_Z = -500;
    public static final int SEG = 20;
    
    private final RandomTeleport plugin;
    private Punto mundo;
    private Punto mundo2;
    private String origen;    
    private String llegada;
    
    public EventosJug(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    public void aplicaExtras(Player pl) {
        pl.setNoDamageTicks(SEG * (int)RandomTeleport.config.
                get(RandomTeleport.TIME_NO_PVP));

        PlayerInventory pi = pl.getInventory();
        if (!pi.contains(Material.BOAT)) {
            pi.addItem(new ItemStack(Material.BOAT, 1));
        }
        
        
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player pl = e.getPlayer();
        
        if(pl.getLocation().getWorld().getName().compareTo((String)RandomTeleport.
                config.get(RandomTeleport.ORIGEN)) == 0)
        {
            Location player = pl.getLocation();
            Punto p = new Punto((int)player.getX(), (int)player.getY(), (int)player.getZ());
            
            try {
                World destino = Bukkit.getServer().
                        getWorld((String)RandomTeleport.config.get(RandomTeleport.DESTINO));
                
                if (RandomTeleport.cubo.compPunto(p)) {
                    pl.sendMessage(Lang.PLAYER_TELEPORTED.getText());
                    Location mundoLlegada = new Location(destino, GenAleatorio.
                            genAl(MAX_X, MIN_X), Y, GenAleatorio.genAl(MAX_Z, MIN_Z));
                    pl.teleport(mundoLlegada);
                    
                    aplicaExtras(pl);
                }                
            }
            catch (Exception ex) {
                pl.sendMessage(PLUGIN + ChatColor.RED + 
                        "El mundo " + llegada + " no existe");
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
                    
                    if(!((Configuracion)lista.get(i)).isParte1() && 
                            e.getMessage().compareToIgnoreCase("marcar") == 0)
                    {
                        origen = e.getPlayer().getWorld().getName();                        
                        mundo = new Punto((int)l.getX(),(int)l.getY(),(int)l.getZ());
                        ((Configuracion)lista.get(i)).setParte1(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Posición actual añadida correctamente");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                                "ahora vaya a la siguiente posicion y ponga \"marcar\" nuevamente");
                    }
                    else if(!((Configuracion)lista.get(i)).isParte2() && 
                            e.getMessage().compareToIgnoreCase("marcar") == 0)
                    {
                        mundo2 = new Punto((int)l.getX(),(int)l.getY(),(int)l.getZ());                        
                        ((Configuracion)lista.get(i)).setParte2(true);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Posición actual añadida correctamente, "
                                + "has creado la zona de teletransporte!!.");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                                "Ahora escriba el nombre donde quieres que "
                                + "los players sean teletransportados.");
                    }
                    else if(e.getMessage().compareToIgnoreCase("reiniciar") == 0)
                    {
                        ((Configuracion)lista.get(i)).desactivar();
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + 
                                "Has reiniciado los datos del plugin.");
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                                "Ahora vaya a una posicion y escriba \"marcar\" "
                                + "para marcar esa posicion");
                    }
                    else if (e.getMessage().compareToIgnoreCase("finalizar") == 0 &&
                            !((Configuracion)lista.get(i)).isParte3())
                    {
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.RED + 
                                "Antes de finalizar tienes que terminar de "
                                + "configurarlo!!, recuerda que puedes usar "
                                + "\"reiniciar\" por si te equivocaste.");
                    }
                    else {
                        plugin.getConfig().set("mundo", origen);                        
                        plugin.getConfig().set("Pos1.x", mundo.getX());
                        plugin.getConfig().set("Pos1.y", mundo.getY());
                        plugin.getConfig().set("Pos1.z", mundo.getZ());
                        plugin.getConfig().set("Pos2.x", mundo2.getX());
                        plugin.getConfig().set("Pos2.y", mundo2.getY());
                        plugin.getConfig().set("Pos2.z", mundo2.getZ());
                        plugin.getConfig().set("llegada", llegada);
                        plugin.saveConfig();
                        lista.remove(i);
                        e.getPlayer().sendMessage(PLUGIN + ChatColor.GOLD + 
                                "Has terminado de configurar el plugin. Ahora haz"
                                + " \"rtreload\" para cargar la nueva configuración");
                    }
                    // Cerrar
                    e.setCancelled(true);
                    break;                    
                }
            }
            else if(((Configuracion)lista.get(i)).isParte2()) {
                
                if(plugin.getServer().getWorld(e.getMessage()) != null)
                {
                    llegada = e.getMessage();
                    ((Configuracion)lista.get(i)).setParte3(true);
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.GREEN + "Mundo " 
                            + e.getMessage() + " añadido correctamente.");
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.BLUE + 
                            "Ahora puedes finalizar la configuracion poniendo finalizar.");
                } 
                else
                {
                    e.getPlayer().sendMessage(PLUGIN + ChatColor.RED + 
                            "El mundo " + e.getMessage() + " no existe");
                }
                
                e.setCancelled(true);
                break;
            }
        }
    }

    @Override
    public void execute(Listener ll, Event event)
        throws EventException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
