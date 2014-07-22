package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 * 
 * @author Julián
 */
public class AuthorCommandExecutor
    implements CommandExecutor
{
    private final RandomTeleport plugin;

    public AuthorCommandExecutor(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[])
    {
        if(args.length > 0)
        {
            sender.sendMessage(PLUGIN + ChatColor.RED + "Te sobran argumentos");
            return false;
        }
        
        sender.sendMessage(PLUGIN + ChatColor.GREEN + "Plugin creado por JuLiTo");
        return true;       
    }
}
