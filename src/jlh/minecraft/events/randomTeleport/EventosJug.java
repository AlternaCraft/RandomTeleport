// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:06:21
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EventosJug.java

package jlh.minecraft.events.randomTeleport;

import java.util.List;
import jlh.minecraft.clases.randomTeleport.*;
import jlh.minecraft.plugin.randomTeleport.ConfigCommandExecutor;
import jlh.minecraft.plugin.randomTeleport.RandomTeleport;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;

public class EventosJug
    implements Listener, EventExecutor
{

    public EventosJug(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(e.getPlayer().getLocation().getWorld().getName().compareTo((String)plugin.config.get(0)) == 0)
        {
            Location player = e.getPlayer().getLocation();
            Punto p = new Punto((int)player.getX(), (int)player.getY(), (int)player.getZ());
            if(plugin.cubo.compPunto(p))
            {
                e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GOLD).append(" ").append("Est\341s siendo teletransportado").toString());
                Location llegada = new Location(Bukkit.getWorld((String)plugin.config.get(3)), GenAleatorio.genAl(10000, -500), 150D, GenAleatorio.genAl(10000, -500));
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
            if(e.getMessage().compareToIgnoreCase("marcar") == 0 || e.getMessage().compareToIgnoreCase("reiniciar") == 0 || e.getMessage().compareToIgnoreCase("finalizar") == 0)
            {
                if(((Configuracion)lista.get(i)).getJugador().equals(e.getPlayer()))
                {
                    Location l = e.getPlayer().getLocation();
                    plugin.getConfig().set("mundo", e.getPlayer().getWorld().getName());
                    plugin.saveConfig();
                    if(!((Configuracion)lista.get(i)).isParte1())
                    {
                        plugin.getConfig().set("Pos1.x", Integer.valueOf((int)l.getX()));
                        plugin.getConfig().set("Pos1.y", Integer.valueOf((int)l.getY()));
                        plugin.getConfig().set("Pos1.z", Integer.valueOf((int)l.getZ()));
                        plugin.saveConfig();
                        ((Configuracion)lista.get(i)).setParte1(true);
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GREEN).append("Posici\363n ").append("actual a\361adida correctamente").toString());
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.BLUE).append("ahora vaya a la ").append("siguiente posicion y ponga \"marcar\" nuevamente").toString());
                        break;
                    }
                    if(!((Configuracion)lista.get(i)).isParte2())
                    {
                        plugin.getConfig().set("Pos2.x", Integer.valueOf((int)l.getX()));
                        plugin.getConfig().set("Pos2.y", Integer.valueOf((int)l.getY()));
                        plugin.getConfig().set("Pos2.z", Integer.valueOf((int)l.getZ()));
                        plugin.saveConfig();
                        ((Configuracion)lista.get(i)).setParte2(true);
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GREEN).append("Posici\363n ").append("actual a\361adida correctamente, has creado la zona ").append("de teletransporte!!.").toString());
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.BLUE).append("Ahora escriba").append(" el nombre donde quieres que los players sean ").append("teletransportados.").toString());
                        break;
                    }
                    if(e.getMessage().compareToIgnoreCase("reiniciar") == 0)
                    {
                        ((Configuracion)lista.get(i)).desactivar();
                        i--;
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GREEN).append("Has ").append("reiniciado los datos del plugin.").toString());
                    } else
                    {
                        lista.remove(i);
                        e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GOLD).append("Has ").append("terminado de configurar el plugin.").toString());
                        break;
                    }
                }
                e.setCancelled(true);
                continue;
            }
            if(!((Configuracion)lista.get(i)).isParte2() || ((Configuracion)lista.get(i)).isParte3())
                continue;
            if(plugin.getServer().getWorld(e.getMessage()) != null)
            {
                plugin.getConfig().set("llegada", e.getMessage());
                plugin.saveConfig();
                ((Configuracion)lista.get(i)).setParte3(true);
                e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GREEN).append("Mundo ").append(e.getMessage()).append(" a\361adido correctamente.").toString());
                e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.BLUE).append("Ahora puedes ").append("finalizar la configuracion poniendo finalizar.").toString());
            } else
            {
                e.getPlayer().sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.RED).append("El mundo ").append(e.getMessage()).append(" no existe").toString());
            }
            e.setCancelled(true);
            break;
        }

    }

    public void execute(Listener ll, Event event)
        throws EventException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static final int MAX_X = 10000;
    public static final int MIN_X = -500;
    public static final int Y = 150;
    public static final int MAX_Z = 10000;
    public static final int MIN_Z = -500;
    private final RandomTeleport plugin;
}
