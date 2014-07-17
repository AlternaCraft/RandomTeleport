package es.jlh.randomTeleport.plugin;

import es.jlh.randomTeleport.events.PlayerChat;
import es.jlh.randomTeleport.events.PlayerTeleport;
import es.jlh.randomTeleport.locales.Lang;
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
        // Llama al manager para que registre los datos del config
        sm.setup(this);
                
        // Eventos de bukkit
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerTeleport(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        sm.getLog().info(Lang.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable() {
        sm.getLog().info(Lang.PLUGIN_DISABLED.getText());
    }
}
