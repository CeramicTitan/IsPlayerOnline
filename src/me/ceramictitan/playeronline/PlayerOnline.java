package me.ceramictitan.playeronline;

import me.ceramictitan.playeronline.listeners.PlayerListener;
import me.ceramictitan.playeronline.listeners.SignListener;
import me.ceramictitan.playeronline.utility.Serialization;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class PlayerOnline extends JavaPlugin implements Listener {
    public Logger log = getLogger();
    FileConfiguration playerData = new YamlConfiguration();
    FileConfiguration locationData = new YamlConfiguration();
    List<Location> locations = new ArrayList<>();

    @Override
    public void onEnable() {
        //Create and load files.
        loadFiles();
        populateLocations();
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        saveLocations();
        saveFiles();

    }

    //Code for saving and loading files, with the properties of a .yml
    private void loadFiles() {
        File pluginDir = new File(getDataFolder(), "Plugin Data");
        File playerDataFile = new File(pluginDir, "PlayerData.yml");
        File locationDataFile = new File(pluginDir, "LocationData.yml");
        try {
            //Load PlayerData.
            playerData.load(playerDataFile);
            log.info("Loaded Player Data File...");
            //Load LocationData
            locationData.load(locationDataFile);
            log.info("Loaded Location Data File...");
        } catch (FileNotFoundException e) {
            saveFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Add defaults to config file.
        getConfig().addDefault("online-message", "&aOnline!");
        getConfig().addDefault("offline-message", "&Offline!");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    //Persistent Data Structures
    private void populateLocations(){
        for(int i = 0; i < getLocationData().getKeys(false).size()+1; i++){
            getLocations().add(Serialization.fromLocationString(getLocationData().getString(String.valueOf(i))));
        }
    }
    //Save Data Structures to file
    private void saveLocations() {
        for (Location loc : getLocations()) {
            int entries = getLocationData().getKeys(false).size();
            getLocationData().set(String.valueOf(entries + 1), Serialization.toLocationString(loc));
        }
        getLocations().clear();
    }
    //Save Changes to files or create new files
    private void saveFiles() {
        File pluginDir = new File(getDataFolder(), "Plugin Data");
        File playerDataFile = new File(pluginDir, "PlayerData.yml");
        File locationDataFile = new File(pluginDir, "LocationData.yml");
        try {
            playerData.save(playerDataFile);
            log.info("Saving Player Data File...");
            locationData.save(locationDataFile);
            log.info("Saving Location Data File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Determine if player is online
    public String getOnlineStatus(Player target) {
        String online = getConfig().getString("online-message");
        String offline = getConfig().getString("offline-message");
        online = ChatColor.translateAlternateColorCodes('&', online);
        offline = ChatColor.translateAlternateColorCodes('&', offline);
        return target.isOnline() ? online : offline;
    }
    //Get locations list
    public List<Location> getLocations() {
        return locations;
    }
    //Get LocationData.yml
    public FileConfiguration getLocationData() {
        return locationData;
    }
    //Get PlayerData.yml
    public FileConfiguration getPlayerData() {
        return playerData;
    }
    //Writes when the player was last seen to the file
    public void setLastSeen(Player player) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String lastSeen = sdf.format(d);
        getPlayerData().set(player.getUniqueId().toString(), lastSeen);
    }
}
