package es.jlh.randomTeleport.plugin;

import es.jlh.randomTeleport.command.AuthorCommandExecutor;
import es.jlh.randomTeleport.command.ConfigCommandExecutor;
import es.jlh.randomTeleport.command.ReloadCommandExecutor;
import es.jlh.randomTeleport.events.EventosJug;
import es.jlh.randomTeleport.locales.Lang;
import es.jlh.randomTeleport.util.Localizacion;
import es.jlh.randomTeleport.util.Punto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Julián
 */
public class RandomTeleport extends JavaPlugin
{
    public static final String PLUGIN = ChatColor.GOLD + "[" + ChatColor.GRAY + 
            "RandomTeleporter" + ChatColor.GOLD + "] ";
    
    public static final int ORIGEN = 0;
    public static final int PUNTO_1 = 1;
    public static final int PUNTO_2 = 2;
    public static final int DESTINO = 3;
    public static final int TIME_NO_PVP = 4;
    
    public static Logger log;
    public static List config = new ArrayList();
    public static Localizacion cubo;
    
    public void cargaClases() {
        log = this.getLogger();
        Lang.load();
    }
    
    public void guardaValores()
    {
        config.clear();
        config.add(getConfig().getString("origen.alias"));
        config.add(new Punto(getConfig().getInt("origen.pos1.x"), 
                getConfig().getInt("origen.pos1.y"), getConfig().getInt("origen.pos1.z")));
        config.add(new Punto(getConfig().getInt("origen.pos2.x"), 
                getConfig().getInt("origen.pos2.y"), getConfig().getInt("origen.pos2.z")));
        config.add(getConfig().getString("destino.llegada"));
        config.add(getConfig().getInt("tiempo.no_pvp"));
    }

    public void cargaConfig()
    {
        getConfig().options().copyDefaults(true);
        guardaValores();
        saveConfig();
    }

    public void cargaComandos()
    {
        getCommand("rt").setExecutor(new AuthorCommandExecutor(this));
        getCommand("rtconfig").setExecutor(new ConfigCommandExecutor(this));
        getCommand("rtreload").setExecutor(new ReloadCommandExecutor(this));
    }

    public void establecePuntos()
    {
        cubo = new Localizacion((Punto)config.get(PUNTO_1), 
                (Punto)config.get(PUNTO_2));
    }

    @Override
    public void onEnable()
    {
        cargaClases();
        cargaConfig();
        cargaComandos();
        establecePuntos();
        
        // Eventos de bukkit
        Bukkit.getServer().getPluginManager().registerEvents(new EventosJug(this), this);
        
        log.info(Lang.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable()
    {
        log.info(Lang.PLUGIN_DISABLED.getText());
    }
}
