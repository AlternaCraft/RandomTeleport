package jlh.minecraft.plugin.randomTeleport;

import java.util.ArrayList;
import java.util.List;
import jlh.minecraft.clases.randomTeleport.Localizacion;
import jlh.minecraft.clases.randomTeleport.Punto;
import jlh.minecraft.events.randomTeleport.EventosJug;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Julián
 */
public class RandomTeleport extends JavaPlugin
{
    public static final String PLUGIN = ChatColor.GOLD + "[" + ChatColor.GRAY + 
            "RandomTeleporter" + ChatColor.GOLD + "] ";;
    public List config;
    public Localizacion cubo;
    
    public RandomTeleport()
    {
        config = new ArrayList();
    }

    public void guardaValores()
    {
        config.add(getConfig().getString("mundo"));
        config.add(new Punto(getConfig().getInt("Pos1.x"), 
                getConfig().getInt("Pos1.y"), getConfig().getInt("Pos1.z")));
        config.add(new Punto(getConfig().getInt("Pos2.x"), 
                getConfig().getInt("Pos2.y"), getConfig().getInt("Pos2.z")));
        config.add(getConfig().getString("llegada"));
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
        cubo = new Localizacion((Punto)config.get(1), (Punto)config.get(2));
    }

    @Override
    public void onEnable()
    {
        cargaConfig();
        cargaComandos();
        establecePuntos();
        Bukkit.getServer().getPluginManager().registerEvents(new EventosJug(this), this);
        Bukkit.getServer().getLogger().info("[RandomTeleport] Activado con exito");
    }

    public void onDisable()
    {
        Bukkit.getServer().getLogger().info("[RandomTeleport] Desactivado con exito");
    }
}
