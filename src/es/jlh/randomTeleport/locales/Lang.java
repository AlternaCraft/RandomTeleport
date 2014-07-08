package es.jlh.randomTeleport.locales;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import java.io.File;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    PLUGIN_ENABLED(PLUGIN + "Activado con exito!"),
    PLUGIN_DISABLED(PLUGIN + "Desactivado con exito!"),
    PLAYER_TELEPORTED(PLUGIN + ChatColor.GOLD + "Preparate para la batalla!!");

    private final String value;
    public static YamlConfiguration config = null;
    public static File configFile = new File("plugins/RandomTeleport/messages.yml");

    private Lang(final String value) {
        this.value = value;
    }

    public String getText() {
        String value = this.getValue();
        if (config != null && config.contains(this.name())) {
                value = config.getString(this.name());
        }
        value = ChatColor.translateAlternateColorCodes('&', value);
        return value;
    }

    public String getValue() {
        return this.value;
    }

    public static void load() {
        if (!configFile.exists()) {
            createConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void createConfig() {
        YamlConfiguration newConfig = new YamlConfiguration();
        
        newConfig.options().header("Mensajes del plugin RandomTeleport");
        newConfig.options().copyHeader(true);
        
        for (Lang lang : Lang.values())
        {
            String name = lang.name();
            String value = lang.getValue();
            newConfig.set(name, value);
        }
        
        try {
            newConfig.save(configFile);
        } 
        catch (Exception e) {
            RandomTeleport.log.log(Level.WARNING, "Error creando los locales del "
                    + "plugin: " + e.getMessage());
        }
    }
}
