package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 * 
 * @author Julián
 */
public class ReloadCommandExecutor
    implements CommandExecutor
{
    private final RandomTeleport plugin;
    
    public ReloadCommandExecutor(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {   
        if(args.length > 0) {
            sender.sendMessage(PLUGIN + Lang.COMMAND_ARGUMENTS.getText());
            return false;
        }
        
        plugin.sm.cargaConfig();
        sender.sendMessage(PLUGIN + Lang.PLUGIN_RELOAD.getText());
        return true;        
    }
}
