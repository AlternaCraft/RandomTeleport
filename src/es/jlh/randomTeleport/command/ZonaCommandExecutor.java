package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import java.util.ArrayList;
import java.util.List;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

/**
 *
 * @author Julián
 */
public class ZonaCommandExecutor implements CommandExecutor {

    public static List lista = new ArrayList();
    private final RandomTeleport plugin;

    public ZonaCommandExecutor(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {

        if (args.length == 0) {
            sender.sendMessage(PLUGIN + ChatColor.RED + "Tienes que indicar "
                    + "crear, borrar o lista al final del comando");
            return false;
        } 
        
        if (args[0].compareToIgnoreCase("crear")==0) {
            
            if (args.length==2) {                
                String resul = compruebaCreacion(args[1], sender);

                if (resul != null) {
                    sender.sendMessage(PLUGIN + ChatColor.RED + resul);
                    return true;
                }

                List<String> zonas = (List<String>)plugin.getConfig().getList("zonasActivas");
                zonas.add(args[1]);
                plugin.saveConfig();
                sender.sendMessage(PLUGIN + ChatColor.GOLD + "Zona " + args[1] + " incluida a "
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
            
            String zonas = "Lista de zonas: ";
            List<String> zonasActivas = (List<String>)plugin.getConfig().getList("zonasActivas");
            
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
        
        if (!zonas.contains(arg) || arg.compareToIgnoreCase("zonasActivas")==0) {
            return false;
        }
        
        return true;
    }    
}
