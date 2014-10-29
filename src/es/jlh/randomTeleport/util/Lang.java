package es.jlh.randomTeleport.util;

import java.io.File;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    PLAYER_TELEPORTED("&6Preparate para la batalla!"),
    PLAYER_INVULNERABILITY("&bEres invulnerable durante %TIME% segundos"),
    PLAYER_NO_PVP("&cNo puedes pegar mientras estas protegido"),
    PLAYER_SI_PVP("&cSe ha terminado tu proteccion"),
    PLUGIN_ERROR_TP("&cEl mundo de destino no existe");

    private final String value;
    public static YamlConfiguration lang = null;
    public static File langFile = new File("plugins/RandomTeleport/messages.yml");

    private Lang(final String value) {
        this.value = value;
    }

    public String getText() {
        String valor = this.getValue();
        if (lang != null && lang.contains(this.name())) {
                valor = lang.getString(this.name());
        }
        valor = ChatColor.translateAlternateColorCodes('&', valor);
        return valor;
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
        
        for (Lang idioma : Lang.values())
        {
            String name = idioma.name();
            String value = idioma.getValue();
            newConfig.set(name, value);
        }
        
        try {
            newConfig.save(langFile);
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
