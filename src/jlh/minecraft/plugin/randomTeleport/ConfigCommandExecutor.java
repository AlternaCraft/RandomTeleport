// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:08:23
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ConfigCommandExecutor.java

package jlh.minecraft.plugin.randomTeleport;

import java.util.ArrayList;
import java.util.List;
import jlh.minecraft.clases.randomTeleport.Configuracion;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

// Referenced classes of package jlh.minecraft.plugin.randomTeleport:
//            RandomTeleport

public class ConfigCommandExecutor
    implements CommandExecutor
{

    public ConfigCommandExecutor(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[])
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Consola no puedes hacer eso").toString());
            return true;
        }
        Player p = (Player)sender;
        if(args.length > 0)
        {
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.RED).append("Te sobran argumentos").toString());
            return false;
        }
        if(lista.contains(p))
        {
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.RED).append("Ya has usado ese comando!").toString());
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.BLUE).append("Puedes volver a empezar escribiendo ").append("reiniciar").toString());
            return true;
        } else
        {
            lista.add(new Configuracion(p, false, false, false));
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.BLUE).append("Ahora vaya a una posicion y ").append("escriba \"marcar\" para marcar esa posicion").toString());
            return true;
        }
    }

    public static List lista = new ArrayList();
    private final RandomTeleport plugin;

}
