// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/07/2014 18:08:37
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RandomTeleport.java

package jlh.minecraft.plugin.randomTeleport;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jlh.minecraft.clases.randomTeleport.Localizacion;
import jlh.minecraft.clases.randomTeleport.Punto;
import jlh.minecraft.events.randomTeleport.EventosJug;
import org.bukkit.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

// Referenced classes of package jlh.minecraft.plugin.randomTeleport:
//            AuthorCommandExecutor, ConfigCommandExecutor, ReloadCommandExecutor

public class RandomTeleport extends JavaPlugin
{

    public RandomTeleport()
    {
        config = new ArrayList();
    }

    public void guardaValores()
    {
        config.add(getConfig().getString("mundo"));
        config.add(new Punto(getConfig().getInt("Pos1.x"), getConfig().getInt("Pos1.y"), getConfig().getInt("Pos1.z")));
        config.add(new Punto(getConfig().getInt("Pos2.x"), getConfig().getInt("Pos2.y"), getConfig().getInt("Pos2.z")));
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

    public static final String PLUGIN;
    public List config;
    public Localizacion cubo;

    static 
    {
        PLUGIN = (new StringBuilder()).append(ChatColor.GOLD).append("[").append(ChatColor.GRAY).append("RandomTeleporter").append(ChatColor.GOLD).append("] ").toString();
    }
}
