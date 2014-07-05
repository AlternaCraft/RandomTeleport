// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:08:47
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReloadCommandExecutor.java

package jlh.minecraft.plugin.randomTeleport;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

// Referenced classes of package jlh.minecraft.plugin.randomTeleport:
//            RandomTeleport

public class ReloadCommandExecutor
    implements CommandExecutor
{

    public ReloadCommandExecutor(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[])
    {
        if(!(sender instanceof Player))
            if(args.length > 0)
            {
                sender.sendMessage("[RandomTeleporter] Te sobran argumentos");
                return false;
            } else
            {
                sender.sendMessage("[RandomTeleporter] Plugin recargado correctamente");
                return true;
            }
        Player p = (Player)sender;
        if(args.length > 0)
        {
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.RED).append("Te sobran argumentos").toString());
            return false;
        } else
        {
            plugin.cargaComandos();
            p.sendMessage((new StringBuilder()).append(RandomTeleport.PLUGIN).append(ChatColor.GREEN).append("Plugin recargado correctamente").toString());
            return true;
        }
    }

    private RandomTeleport plugin;
}
