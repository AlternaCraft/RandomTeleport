package jlh.minecraft.plugin.randomTeleport;

import java.util.ArrayList;
import java.util.List;
import jlh.minecraft.clases.randomTeleport.Configuracion;
import static jlh.minecraft.plugin.randomTeleport.RandomTeleport.PLUGIN;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 * 
 * @author Julián
 */
public class ConfigCommandExecutor
    implements CommandExecutor
{
    public static List lista = new ArrayList();
    private final RandomTeleport plugin;
    
    public ConfigCommandExecutor(RandomTeleport plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String args[])
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Consola no puedes hacer eso");
            return true;
        }
        
        Player p = (Player)sender;
        if(args.length > 0)
        {
            p.sendMessage(PLUGIN + ChatColor.RED + "Te sobran argumentos");
            return false;
        }
        if(lista.contains(p))
        {
            p.sendMessage(PLUGIN + ChatColor.RED + "Ya has usado ese comando!");
            p.sendMessage(PLUGIN + ChatColor.BLUE + "Puedes volver a empezar escribiendo \"reiniciar\"");
            return true;
        } else
        {
            lista.add(new Configuracion(p, false, false, false));
            p.sendMessage(PLUGIN + ChatColor.BLUE + "Ahora vaya a una posicion y"
                    + " escriba \"marcar\" para marcar esa posicion");
            return true;
        }
    }
}
