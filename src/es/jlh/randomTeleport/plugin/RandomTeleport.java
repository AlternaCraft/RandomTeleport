package es.jlh.randomTeleport.plugin;

import es.jlh.randomTeleport.command.InfoCommandExecutor;
import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.command.PurgeCommandExecutor;
import es.jlh.randomTeleport.command.ReloadCommandExecutor;
import es.jlh.randomTeleport.command.ZonaCommandExecutor;
import es.jlh.randomTeleport.handlers.HandleChat;
import es.jlh.randomTeleport.handlers.HandleTeleport;
import es.jlh.randomTeleport.util.Lang;
import es.jlh.randomTeleport.util.Metrics;
import java.io.IOException;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Julián
 */
public class RandomTeleport extends JavaPlugin {

    public static final String PLUGIN = ChatColor.GOLD + "[" + ChatColor.GRAY
            + "RandomTeleporter" + ChatColor.GOLD + "] ";
    
    public RandomTeleportManager sm = RandomTeleportManager.getInstance();   

    @Override
    public void onEnable() {        
        // Cargo los locales
        try {            
            Lang.load();
        } 
        catch (Exception ex) {
            this.getServer().getConsoleSender().sendMessage(PLUGIN + 
                        ChatColor.RED + ex.getMessage());
        }
        
        // Metricas del plugin
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } 
        catch (IOException e) {
            this.getServer().getConsoleSender().sendMessage(PLUGIN + 
                        ChatColor.RED + "Failed to submit the stats :-("); 
       }        
        
        // Llama al manager para que registre los datos del config
        sm.setup(this);
        
        // Comandos del plugin
        this.getCommand("rt").setExecutor(new InfoCommandExecutor(this));
        this.getCommand("rtconfig").setExecutor(new ConfigCommandExecutor(this));
        this.getCommand("rtzone").setExecutor(new ZonaCommandExecutor(this));
        this.getCommand("rtpurge").setExecutor(new PurgeCommandExecutor(this));
        this.getCommand("rtreload").setExecutor(new ReloadCommandExecutor(this));
        
        // Handlers
        Bukkit.getServer().getPluginManager().registerEvents(new HandleTeleport(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HandleChat(this), this);
        
        sm.getLog().info(Lang.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable() {
        sm.getLog().info(Lang.PLUGIN_DISABLED.getText());
    }
}
