package me.ceramictitan.playeronline.utility;

import me.ceramictitan.playeronline.PlayerOnline;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.List;

//Changes the status of the player on sign.
public class SignUpdater {
    public static void updateSigns(Player player, PlayerOnline plugin){
        List<Location> locations = plugin.getLocations();
        for(Location loc : locations){
           Block b = loc.getWorld().getBlockAt(loc);
            if(b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) b.getState();
                if(sign.getLine(1).equalsIgnoreCase(player.getDisplayName())){
                    sign.setLine(2, plugin.getOnlineStatus(player));
                    plugin.log.info("Sign changed");
                }
            }
        }
    }
}
