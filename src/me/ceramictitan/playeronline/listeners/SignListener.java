package me.ceramictitan.playeronline.listeners;


import me.ceramictitan.playeronline.PlayerOnline;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    public PlayerOnline plugin;

    public SignListener(PlayerOnline plugin) {
        this.plugin = plugin;
    }

    //Listens to when the sign is created
    @EventHandler
    public void onSignUpdate(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[online]")) {
            if (p.hasPermission("ipo.sign.create")) {
                if (e.getLine(1) != null) {
                    if (p.hasPermission("ipo.sign.others")) {
                        Player target = Bukkit.getPlayer(e.getLine(1));
                        String onlineMsg = plugin.getOnlineStatus(target);
                        e.setLine(0, ChatColor.BLACK + "[" + ChatColor.YELLOW + "Online" + ChatColor.BLACK + "]");
                        e.setLine(1, target.getDisplayName());
                        e.setLine(2, onlineMsg);
                        plugin.getLocations().add(e.getBlock().getLocation());
                    } else {
                        p.sendMessage(ChatColor.RED + "Insufficient permissions!");
                    }
                } else {
                    e.setLine(0, ChatColor.BLACK + "[" + ChatColor.YELLOW + "Online" + ChatColor.BLACK + "]");
                    e.setLine(1, p.getDisplayName());
                    e.setLine(2, plugin.getOnlineStatus(p));
                    plugin.getLocations().add(e.getBlock().getLocation());
                }
            } else {
                p.sendMessage(ChatColor.RED + "Insufficient Permission!");
            }


        }
    }

    //When sign is clicked, the user is prompted with the last seen information
    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.SIGN) {
                Player player = e.getPlayer();
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[online]")) {
                    Player target = Bukkit.getPlayer(sign.getLine(1));
                    String lastSeen = plugin.getPlayerData().getString(target.getUniqueId().toString());
                    if (!target.isOnline()) {
                        if (lastSeen != null) {
                            player.sendMessage(target.getDisplayName() + " was last seen: " + lastSeen);
                        } else {
                            player.sendMessage("That player has never been seen before!");
                        }
                    } else {
                        player.sendMessage("That player is online!");
                    }
                }
            }
        }
    }

    //Handles when the sign is broken
    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if (plugin.getLocations().contains(e.getBlock().getLocation())) {
            plugin.getLocations().remove(e.getBlock().getLocation());
        }
    }
}