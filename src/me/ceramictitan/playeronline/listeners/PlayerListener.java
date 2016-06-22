package me.ceramictitan.playeronline.listeners;

import me.ceramictitan.playeronline.PlayerOnline;
import me.ceramictitan.playeronline.utility.SignUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    PlayerOnline plugin;

    public PlayerListener(PlayerOnline plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        SignUpdater.updateSigns(e.getPlayer(),plugin);
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        SignUpdater.updateSigns(e.getPlayer(),plugin);
        plugin.setLastSeen(e.getPlayer());
    }
}
