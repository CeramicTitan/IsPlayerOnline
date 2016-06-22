package me.ceramictitan.playeronline.utility;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Serialization {
    //Converts Location to a String instance
    public static String toLocationString(Location loc){
        return loc.getX()+":"+loc.getY()+":"+loc.getZ()+"="+loc.getWorld().getName();
    }
    //Converts String to a Location instance
    public static Location fromLocationString(String string){
        String[] coords = string.split(":");
        Double x =Double.parseDouble(coords[0]);
        Double y =Double.parseDouble(coords[1]);
        Double z =Double.parseDouble(coords[2].split("=")[0]);
        World world = Bukkit.getWorld(string.split("=")[1]);
        return new Location(world,x,y,z);
    }
}
