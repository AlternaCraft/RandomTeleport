package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Lang;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 
 * @author Julián
 */
public class PurgeCommandExecutor implements CommandExecutor {
    private final RandomTeleport plugin;
    
    public PurgeCommandExecutor(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {
        if(args.length > 0) {
            sender.sendMessage(PLUGIN + Lang.COMMAND_ARGUMENTS.getText());
            return false;
        }
        
        List<String> zonas = plugin.getConfig().getStringList("zonasActivas");
        
        //Edito el config
        FileConfiguration conf = new YamlConfiguration();
        FileConfiguration miconf = plugin.getConfig();

        conf.options().header(miconf.options().header());
        conf.set("zonasActivas",zonas);
        
        for (String zona : zonas) {
            conf.set(zona + ".tiempo.no_pvp", miconf.get(zona + ".tiempo.no_pvp"));
            conf.set(zona + ".origen.alias", miconf.get(zona + ".origen.alias"));
            conf.set(zona + ".origen.pos1.x", miconf.get(zona + ".origen.pos1.x"));
            conf.set(zona + ".origen.pos1.y", miconf.get(zona + ".origen.pos1.y"));
            conf.set(zona + ".origen.pos1.z", miconf.get(zona + ".origen.pos1.z"));
            conf.set(zona + ".origen.pos2.x", miconf.get(zona + ".origen.pos2.x"));
            conf.set(zona + ".origen.pos2.y", miconf.get(zona + ".origen.pos2.y"));
            conf.set(zona + ".origen.pos2.z", miconf.get(zona + ".origen.pos2.z"));
            conf.set(zona + ".destino.alias", miconf.get(zona + ".destino.alias"));
            if (miconf.get(zona + ".subzona") != null) {
                int j = 0;
                do {
                    if (miconf.get(zona + ".subzona.zona" + j) == null) {
                        break;
                    }
                    conf.set(zona + ".subzona.zona" + j + ".alias", miconf.get(zona + ".subzona.zona" + j + ".alias"));
                    conf.set(zona + ".subzona.zona" + j + ".pos1.x", miconf.get(zona + ".subzona.zona" + j + ".pos1.x"));
                    conf.set(zona + ".subzona.zona" + j + ".pos1.y", miconf.get(zona + ".subzona.zona" + j + ".pos1.y"));
                    conf.set(zona + ".subzona.zona" + j + ".pos1.z", miconf.get(zona + ".subzona.zona" + j + ".pos1.z"));
                    conf.set(zona + ".subzona.zona" + j + ".pos2.x", miconf.get(zona + ".subzona.zona" + j + ".pos2.x"));
                    conf.set(zona + ".subzona.zona" + j + ".pos2.y", miconf.get(zona + ".subzona.zona" + j + ".pos2.y"));
                    conf.set(zona + ".subzona.zona" + j + ".pos2.z", miconf.get(zona + ".subzona.zona" + j + ".pos2.z"));
                    j++;
                } 
                while (true);
            }
        }
        
        try {
            conf.save(new StringBuilder().append(plugin.getDataFolder()).append(File.separator).append("config.yml").toString());
            sender.sendMessage(PLUGIN + Lang.PLUGIN_CLEANED.getText());
        } 
        catch (IOException ex) {
            sender.sendMessage(PLUGIN + Lang.PLUGIN_NO_CLEANED.getText());
        }       
        return true;
    }
}
