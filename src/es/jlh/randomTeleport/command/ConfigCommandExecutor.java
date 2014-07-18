package es.jlh.randomTeleport.command;

import es.jlh.randomTeleport.plugin.RandomTeleport;
import java.util.ArrayList;
import java.util.List;
import es.jlh.randomTeleport.util.Configuracion;
import static es.jlh.randomTeleport.plugin.RandomTeleport.PLUGIN;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 *
 * @author Julián
 */
public class ConfigCommandExecutor implements CommandExecutor {

    public static List<Configuracion> lista = new ArrayList();
    private final RandomTeleport plugin;

    public ConfigCommandExecutor(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[]) {
        // Añadir nombre de zona para varias zonas posibles en el config
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Consola no puedes hacer eso");
            return true;
        }

        Player p = (Player) sender;

        // Compruebo los argumentos
        if (args.length < 1) {
            p.sendMessage(PLUGIN + ChatColor.RED + "Tienes que indicar el nombre"
                    + " de la zona");
            return false;
        } 
        else if (args.length > 1) {
            p.sendMessage(PLUGIN + ChatColor.RED + "Te sobran argumentos");
            return false;
        }
        
        // Nombre reservado        
        if (args[0].compareToIgnoreCase("zonasActivas")==0) {
            p.sendMessage(PLUGIN + ChatColor.RED + "Ese nombre esta reservado");
            return true;
        }

        // Si ya existe le aviso
        List<String> zonas = (List<String>)plugin.getConfig().getList("zonasActivas");        
        if (zonas.contains(args[0])) {
            p.sendMessage(PLUGIN + ChatColor.RED + "La zona " + args[0] + " ya existe asi que "
                    + "sobreescribiran los datos");
        }
        
        // Si ya esta configurando no puede empezar de nuevo
        for (Configuracion conf : lista) {
            if (conf.getJugador().equals(p)) {
                p.sendMessage(PLUGIN + ChatColor.RED + "Ya has usado ese comando!");
                p.sendMessage(PLUGIN + ChatColor.BLUE + "Puedes volver a empezar "
                        + "escribiendo \"reiniciar\"");
                return true;
            }
        }

        Configuracion conf = new Configuracion(p, false, false, false, false, false, false);
        conf.getL().setZona(args[0]);
        
        lista.add(conf);
        
        p.sendMessage(PLUGIN + ChatColor.BLUE + "Ahora vaya a una posicion y"
                + " escriba \"marcar\" para marcar esa posicion");
        p.sendMessage(PLUGIN + ChatColor.DARK_AQUA + "Recuerda que puedes parar de "
                + "configurar usando \"cancelar\"");
        return true;        
    }
}
