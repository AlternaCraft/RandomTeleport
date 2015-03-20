package es.jlh.randomTeleport.util;

import java.io.File;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    PLUGIN_ENABLED("Plugin activado correctamente"),
    PLUGIN_DISABLED("Plugin desactivado correctamente"),
    PLUGIN_CLEANED("&6Plugin limpiado correctamente"),
    PLUGIN_NO_CLEANED("&4No se ha podido purgar la configuracion"),
    PLUGIN_RELOAD("&aPlugin recargado correctamente"),
    COMMAND_FORBIDDEN("&4No puedes ejecutar ese comando"),
    COMMAND_ARGUMENTS("&4Te faltan/sobran argumentos!"),
    COMMAND_CONFIG_INFO("Para crear la zona"),
    COMMAND_PURGE_INFO("Para limpiar las zonas NO activas"),
    COMMAND_RELOAD_INFO("Para recargar el config del plugin"),
    COMMAND_ZONE_INFO("Para gestionar las zonas"),    
    PLAYER_TELEPORTED("&6Preparate para la batalla!"),
    PLAYER_INVULNERABILITY("&bEres invulnerable durante %TIME% segundos"),
    PLAYER_NO_PVP("&cNo puedes pegar mientras estas protegido"),
    PLAYER_SI_PVP("&cSe ha terminado tu proteccion"),
    PLUGIN_ERROR_TP("&cEl mundo de destino no existe");

    private final String value;
    public static YamlConfiguration lang = null;
    public static File langFile = new File("plugins/RandomTeleport/messages.yml");
    public static File backupFile = new File("plugins/PvpTitles/messages.backup.yml");

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

    private static boolean compLocales() {
        // Compruebo si esta completo
        for (Lang idioma : Lang.values()) {
            if (!lang.contains(idioma.name())) {                
                try {
                    lang.save(backupFile); // Guardo una copia de seguridad                    
                    createConfig();
                    lang = YamlConfiguration.loadConfiguration(langFile);
                    return false;
                } 
                catch (IOException ex) {}
            }
        }
        return true;
    }    
    
    public static void load() throws Exception {
        if (!langFile.exists()) {
            createConfig();
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
        
        if (!compLocales()) {
            throw new Exception("Error loading locales, a new one has been created.");
        } 
    }

    public static void createConfig() {
        YamlConfiguration newConfig = new YamlConfiguration();
        
        newConfig.options().header (
            "########################################\n" + 
            "## Mensajes del plugin RandomTeleport ##\n" +
            "########################################"
        );
        newConfig.options().copyHeader(true);
        
        for (Lang idioma : Lang.values()) {
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
