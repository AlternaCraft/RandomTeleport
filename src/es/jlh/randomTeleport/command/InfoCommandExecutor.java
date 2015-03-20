package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

/**
 * 
 * @author Julián
 */
public class InfoCommandExecutor implements CommandExecutor {
    private final RandomTeleport plugin;

    public InfoCommandExecutor(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {
        if(args.length > 0) {
            sender.sendMessage(PLUGIN + Lang.COMMAND_ARGUMENTS.getText());
            return false;
        }
        
        sender.sendMessage("");
        
        sender.sendMessage(PLUGIN + ChatColor.YELLOW + "v" + plugin.getDescription().getVersion());
        
        if (sender.hasPermission("rt.config")) {
            sender.sendMessage("  " + ChatColor.AQUA + "/rtconfig <zone_name>"
                    + ChatColor.RESET + " ["+Lang.COMMAND_CONFIG_INFO.getText()+"]");
        }
        
        if (sender.hasPermission("rt.purge")) {
            sender.sendMessage("  " + ChatColor.AQUA + "/rtpurge " + 
                    ChatColor.RESET + " ["+Lang.COMMAND_PURGE_INFO.getText()+"]");
        }
        
        if (sender.hasPermission("rt.reload")) {
            sender.sendMessage("  " + ChatColor.AQUA + "/rtreload " + 
                    ChatColor.RESET + " ["+Lang.COMMAND_RELOAD_INFO.getText()+"]");
        }     
        
        if (sender.hasPermission("rt.zone")) {
            sender.sendMessage("  " + ChatColor.AQUA + "/rtzone <activar/borrar/ver/lista> <zone_name> <time> " + 
                    ChatColor.RESET + " ["+Lang.COMMAND_ZONE_INFO.getText()+"]");
        }     
        
        sender.sendMessage("■ " + ChatColor.GOLD + "Creado por Julito" + 
                ChatColor.RESET + " ■");
        return true;       
    }
}
