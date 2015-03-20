package es.jlh.randomTeleport.command;

import static es.jlh.randomTeleport.handlers.HandleTeleport.TICKS;
import es.jlh.randomTeleport.plugin.RandomTeleport;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import es.jlh.randomTeleport.util.Punto;
import es.jlh.randomTeleport.util.Zona;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 *
 * @author Julián
 */
public class ZonaCommandExecutor implements CommandExecutor {    
    public static List lista = new ArrayList();
    private final RandomTeleport plugin;
    
    private final ArrayList<String> zonasUsadas = new ArrayList();
    
    public ZonaCommandExecutor(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {
        if (args.length == 0) {
            sender.sendMessage(PLUGIN + ChatColor.RED + "Tienes que indicar "
                    + "activar, borrar, ver o lista al final del comando");
            return false;
        } 
        
        if (args[0].compareToIgnoreCase("activar") == 0) {            
            if (args.length==2) {                
                String resul = compruebaCreacion(args[1], sender);

                if (resul != null) {
                    sender.sendMessage(PLUGIN + ChatColor.RED + resul);
                    return true;
                }
                
                List<String> zonasActivas = (List<String>)plugin.getConfig().getList("zonasActivas");                
                zonasActivas.add(args[1]);
                
                plugin.saveConfig();
                
                sender.sendMessage(PLUGIN + ChatColor.GOLD + "Zona \"" + args[1] + "\" incluida a "
                        + "las zonas activas");
                sender.sendMessage(PLUGIN + ChatColor.BLUE + "Haz \"/rtreload\" para "
                        + "aplicar los cambios");

                return true;          
            }
            
            return false;
        }
        else if (args[0].compareToIgnoreCase("borrar")==0) {
            if (args.length==2) {
                if (!compruebaEliminacion(args[1], sender)) {
                    sender.sendMessage(PLUGIN + ChatColor.RED + "La zona " + args[1] + " no existe");
                    return true;
                }

                plugin.getConfig().getList("zonasActivas").remove(args[1]);
                plugin.saveConfig();
                
                sender.sendMessage(PLUGIN + ChatColor.GOLD + "Zona " + args[1] + " eliminada de "
                        + "las zonas activas, puede eliminar su configuracion si lo desea");
                sender.sendMessage(PLUGIN + ChatColor.BLUE + "Haz \"/rtreload\" para "
                        + "aplicar los cambios");                

                return true;
            }
            return false;
        }
        else if (args[0].compareToIgnoreCase("lista")==0) {
            if (args.length>1) {
                sender.sendMessage(PLUGIN + ChatColor.RED + "Te sobran argumentos");
                return false;
            }
            
            List<String> zonasActivas = (List<String>)plugin.getConfig().getList("zonasActivas");
            
            String zonas = "Lista de zonas: ";            
            
            for (int i = 0; i < zonasActivas.size(); i++) {
                if (i==zonasActivas.size()-1) {
                    zonas += zonasActivas.get(i)+".";
                    break;
                }
                zonas += zonasActivas.get(i)+", ";
            }
            
            sender.sendMessage(PLUGIN + ChatColor.GOLD + zonas);
            
            return true;
        }
        else if (args[0].compareToIgnoreCase("ver")==0) {
            
            if (!(sender instanceof Player)) {
                sender.sendMessage(PLUGIN + ChatColor.RED + "Este comando solo "
                        + "puede ser ejecutado por un jugador");
            }
            
            final Player pl = (Player) sender;
            
            if (args.length==3) {                
                String resul = compruebaVista(args[1], sender);

                if (resul != null) {
                    pl.sendMessage(PLUGIN + ChatColor.RED + resul);
                    return true;
                }

                if (zonasUsadas.contains(args[1])) {
                    pl.sendMessage(PLUGIN + ChatColor.RED + "La zona ya está en "
                            + "uso");
                    return true;
                }
                
                int tiempo;                
                try {
                    tiempo = Integer.parseInt(args[2]);
                    if (tiempo < 15 || tiempo > 60) {
                        tiempo = 15;
                        pl.sendMessage(PLUGIN + ChatColor.RED + "Cantidad de tiempo "
                                + "incorrecta se ha establecido por defecto a 15s");
                    }
                }
                catch(NumberFormatException ex) {
                    return false;
                }
                
                Punto p1 = new Punto (
                    plugin.getConfig().getInt(args[1]+".origen.pos1.x"),
                    plugin.getConfig().getInt(args[1]+".origen.pos1.y"),
                    plugin.getConfig().getInt(args[1]+".origen.pos1.z")
                );

                Punto p2 = new Punto (
                    plugin.getConfig().getInt(args[1]+".origen.pos2.x"),
                    plugin.getConfig().getInt(args[1]+".origen.pos2.y"),
                    plugin.getConfig().getInt(args[1]+".origen.pos2.z")
                );

                final Zona pr = new Zona(p1,p2);
                
                World mundo = Bukkit.getWorld((String)plugin.getConfig().get(args[1]+".origen.alias"));
                
                pl.setGameMode(GameMode.CREATIVE);
                pl.teleport(pr.visitarZona(mundo));
                
                zonasUsadas.add(args[1]);
                
                pr.generaBlocks(mundo);
                pl.sendMessage(PLUGIN + ChatColor.GREEN + "Zona marcada con bloques "
                        + "de glowstone, se restablecera en " + tiempo + " segundos...");

                final String argumento = args[1];
                
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            pr.devuelveBlocks();
                            zonasUsadas.remove(argumento);
                            pl.sendMessage(PLUGIN + ChatColor.GOLD + "La zona "
                                    + "volvió a como estaba por defecto");                            
                        }
                }, TICKS * tiempo);  
                
                return true;                
            }            
            return false;
        }        
        return false;
    }
    
    public String compruebaCreacion(String arg, CommandSender p) {
        List<String> zonas = (List<String>)plugin.getConfig().getList("zonasActivas");
        
        if (zonas.contains(arg) || arg.compareToIgnoreCase("zonasActivas")==0) {
            return "La zona " + arg + " ya existe";
        }        
        else if (plugin.getConfig().get(arg)==null) {
            return "La zona " + arg + " no se encuentra definida";
        }
        
        return null;
    }
    
    public boolean compruebaEliminacion(String arg, CommandSender p) {
        List<String> zonas = (List<String>)plugin.getConfig().getList("zonasActivas");
        
        if (!zonas.contains(arg) || arg.compareToIgnoreCase("zonasActivas") == 0) {
            return false;
        }
        
        return true;
    }
    
    public String compruebaVista(String arg, CommandSender p) {
        List<String> zonas = plugin.getConfig().getStringList("zonasActivas");
        
        if (!zonas.contains(arg) || arg.compareToIgnoreCase("zonasActivas")==0) {
            return "La zona " + arg + " no existe o no se puede utilizar";
        }        
        else if (plugin.getConfig().get(arg)==null) {
            return "La zona " + arg + " no se encuentra definida";
        }       
        
        return null;
    }
}
