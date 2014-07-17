/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.jlh.randomTeleport.plugin;

import es.jlh.randomTeleport.command.ZonaCommandExecutor;
import es.jlh.randomTeleport.command.AuthorCommandExecutor;
import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.command.ReloadCommandExecutor;
import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.util.Localizacion;
import es.jlh.randomTeleport.util.Punto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Julian
 */
public class RandomTeleportManager {
    
    public static final String HEADER = 
        "###############################################\n" +
        "#######  CONFIG RANDOM TELEPORT 1.7.X  ########\n" +
        "###############################################\n" +
        "##         *** Creado por JuLiTo ***         ##\n" +
        "##              - 1.0 | 1.7.X -              ##\n" +
        "##        alternativecraft@hotmail.es        ##\n" +
        "###############################################\n" +
        "#############  INFORMACIÓN EXTRA  #############\n" +
        "###############################################\n" +
        "##   El tiempo de proteccion es en segundos  ##\n" +
        "##        Las coordenadas son enteras        ##\n" +
        "###############################################\n" +
        "###############  ¡¡DISFRÚTALO!!  ##############\n" +
        "###############################################\n\n" +
        "SI EL SERVER ESTA ENCENDIDO NO EDITES EL FICHERO DIRECTAMENTE";
    
    public static RandomTeleportManager instance = new RandomTeleportManager();
    
    private RandomTeleport plugin; 
    
    private FileConfiguration config;    
    private Logger log;
    
    private List<Localizacion> configuration = new ArrayList();
    
    /**
     * Constructor de la clase
     */
    private RandomTeleportManager() {}

    public static RandomTeleportManager getInstance() {
        return instance;
    }

    /*
     * Lista de metodo para obtener los datos del config, el cubo que se genera, 
     * y el log del server
     */
    
    public List getConfiguration() {
        return configuration;
    }

    public Logger getLog() {
        return log;
    }
    
    /*
     * Descripcion del plugin
     */
    
    public PluginDescriptionFile getDesc() {
            return plugin.getDescription();
    }
    
    /*
     *  Metodo principal para ejecutar todas las configuraciones
     */
    
    public void setup(RandomTeleport plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
        
        Lang.load();
        
        cargaConfig();
        cargaComandos(); // Asocio los comandos con los ejecutores
    }
    
    public void cargaConfig() {
        plugin.getConfig().options().header(HEADER);
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        
        config = plugin.getConfig();
        guardaValores(); // Guarda los datos del config en una lista
    }
    
    private void cargaComandos() {
        plugin.getCommand("rt").setExecutor(new AuthorCommandExecutor(plugin));
        plugin.getCommand("rtconfig").setExecutor(new ConfigCommandExecutor(plugin));
        plugin.getCommand("rtzone").setExecutor(new ZonaCommandExecutor(plugin));
        plugin.getCommand("rtreload").setExecutor(new ReloadCommandExecutor(plugin));
    }
    
    private void guardaValores() {
        List<String> zonas = (List<String>)config.getList("zonasActivas");
        configuration.clear();
        
        for (int i = 0; i < zonas.size(); i++) {
            String zona = zonas.get(i);
            
            if (config.get(zona)==null) {
                plugin.getConfig().getList("zonasActivas").remove(zona);
                plugin.saveConfig();
                Bukkit.getServer().broadcastMessage("Zonas "+zona+" borrada");
            }
            else {            
                Punto p1 = new Punto(config.getInt(zona + ".origen.pos1.x"), 
                        config.getInt(zona + ".origen.pos1.y"), config.getInt(zona + ".origen.pos1.z"));
                Punto p2 = new Punto(config.getInt(zona + ".origen.pos2.x"), 
                        config.getInt(zona + ".origen.pos2.y"), config.getInt(zona + ".origen.pos2.z"));

                String origen = config.getString(zona + ".origen.alias");
                String llegada = config.getString(zona + ".destino.alias");

                int nopvp = config.getInt(zona + ".tiempo.no_pvp");

                configuration.add(new Localizacion(zona,p1,p2,origen,llegada,nopvp));
            }
        }
    }
}