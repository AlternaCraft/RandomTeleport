package es.jlh.randomTeleport.locales;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    PLUGIN_ENABLED("Activado con exito!"),
    PLUGIN_DISABLED("Desactivado con exito!"),
    PLAYER_TELEPORTED("Preparate para la batalla!"),
    PLAYER_INVULNERABILITY("Eres invulnerable durante %TIME% segundos"),
    PLAYER_NO_PVP("No puedes pegar mientras estas protegido"),
    PLAYER_SI_PVP("Se ha terminado tu proteccion");

    private final String value;
    public static YamlConfiguration lang = null;
    public static File langFile = new File("plugins/RandomTeleport/messages.yml");

    private Lang(final String value) {
        this.value = value;
    }

    public String getText() {
        String value = this.getValue();
        if (lang != null && lang.contains(this.name())) {
                value = lang.getString(this.name());
        }
        //value = ChatColor.translateAlternateColorCodes('&', value);
        return value;
    }

    public String getValue() {
        return this.value;
    }

    public static void load() {
        if (!langFile.exists()) {
            createConfig();
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public static void createConfig() {
        YamlConfiguration newConfig = new YamlConfiguration();
        
        newConfig.options().header (
            "########################################\n" + 
            "## Mensajes del plugin RandomTeleport ##\n" +
            "########################################"
        );
        newConfig.options().copyHeader(true);
        
        for (Lang lang : Lang.values())
        {
            String name = lang.name();
            String value = lang.getValue();
            newConfig.set(name, value);
        }
        
        try {
            newConfig.save(langFile);
        } 
        catch (Exception e) {
            // a
        }
    }
}
