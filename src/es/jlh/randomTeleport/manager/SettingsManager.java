/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.jlh.randomTeleport.manager;

import es.jlh.randomTeleport.command.AuthorCommandExecutor;
import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.command.ReloadCommandExecutor;
import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PUNTO_1;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PUNTO_2;
import es.jlh.randomTeleport.util.Localizacion;
import es.jlh.randomTeleport.util.Punto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Julian
 */
public class SettingsManager {
   
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
        "##     Las coordenadas deben ser enteras     ##\n" +
        "###############################################\n" +
        "###############  ¡¡DISFRÚTALO!!  ##############\n" +
        "###############################################";
    
    public static SettingsManager instance = new SettingsManager();
    
    private RandomTeleport plugin;
    
    private FileConfiguration config;    
    private Logger log;
    private List configuration = new ArrayList();
    private Localizacion cubo;    
    
    /**
     * Constructor de la clase
     */
    private SettingsManager() {}

    public static SettingsManager getInstance() {
        return instance;
    }

    /*
        Lista de metodo para obtener los datos del config, el cubo que se genera, 
        y el log del server
    */
    
    public List getConfiguration() {
        return configuration;
    }

    public Localizacion getCubo() {
        return cubo;
    }

    public Logger getLog() {
        return log;
    }

    /*
        Metodo principal para ejecutar todas las configuraciones
    */
    
    public void comenzar(RandomTeleport plugin) {
        this.plugin = plugin;
        cargaConfig();
        cargaClases();
        cargaComandos(); // Asocio los comandos con los ejecutores
    }
    
    public void cargaConfig() {
        plugin.getConfig().options().header(HEADER);
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        
        config = plugin.getConfig();
        guardaValores(); // Guarda los datos del config en una lista
        establecePuntos(); // Creo el cubo
    }
    
    private void cargaClases() {
        log = plugin.getLogger();
        Lang.load();
    }
    
    private void cargaComandos() {
        plugin.getCommand("rt").setExecutor(new AuthorCommandExecutor(plugin));
        plugin.getCommand("rtconfig").setExecutor(new ConfigCommandExecutor(plugin));
        plugin.getCommand("rtreload").setExecutor(new ReloadCommandExecutor(plugin));
    }
    
    private void guardaValores() {
        configuration.clear();
        configuration.add(config.getString("origen.alias"));
        configuration.add(new Punto(config.getInt("origen.pos1.x"),
                config.getInt("origen.pos1.y"), config.getInt("origen.pos1.z")));
        configuration.add(new Punto(config.getInt("origen.pos2.x"),
                config.getInt("origen.pos2.y"), config.getInt("origen.pos2.z")));
        configuration.add(config.getString("destino.alias"));
        configuration.add(config.getInt("tiempo.no_pvp"));
    }
    
    private void establecePuntos() {
        cubo = new Localizacion((Punto) configuration.get(PUNTO_1),
                (Punto) configuration.get(PUNTO_2));
    }
}