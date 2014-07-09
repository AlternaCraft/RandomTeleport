package es.jlh.randomTeleport.plugin;

import es.jlh.randomTeleport.playerEvents.PlayerChat;
import es.jlh.randomTeleport.playerEvents.PlayerMove;
import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.manager.SettingsManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Julián
 */
public class RandomTeleport extends JavaPlugin {

    public static final String PLUGIN = ChatColor.GOLD + "[" + ChatColor.GRAY
            + "RandomTeleporter" + ChatColor.GOLD + "] ";
    
    public static final int ORIGEN = 0;
    public static final int PUNTO_1 = 1;
    public static final int PUNTO_2 = 2;
    public static final int DESTINO = 3;
    public static final int TIME_NO_PVP = 4;
    
    public SettingsManager sm = SettingsManager.getInstance();   

    @Override
    public void onEnable() {
        sm.comenzar(this);
        // Eventos de bukkit
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        sm.getLog().info(Lang.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable() {
        sm.getLog().info(Lang.PLUGIN_DISABLED.getText());
    }
}
