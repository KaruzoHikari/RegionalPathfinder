package com.mattymatty.RegionalPathfinder;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationPair {

    private Location minCorner;
    private Location maxCorner;

    public LocationPair(Location minCorner, Location maxCorner) {
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public Location getMinCorner() { return minCorner; }
    public Location getMaxCorner() { return maxCorner; }

    public boolean containsLocation(Location location) {
        if(location.getBlockY() >= minCorner.getBlockY() && location.getBlockX() <= maxCorner.getBlockY()) {
            if(location.getBlockX() >= minCorner.getBlockX() && location.getBlockX() <= maxCorner.getBlockX()) {
                if(location.getBlockZ() >= minCorner.getBlockZ() && location.getBlockZ() <= maxCorner.getBlockZ()) {
                    return true;
                }
            }
        }
        return false;
    }

    //USE AS LAST RESOURCE AS POSSIBLE
    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<>();
        for (double x = minCorner.getBlockX(); x <= maxCorner.getBlockX(); x++) {
            for (double y = minCorner.getBlockY(); y <= maxCorner.getBlockY(); y++) {
                for (double z = minCorner.getBlockZ(); z <= maxCorner.getBlockZ(); z++) {
                    locationList.add(new Location(minCorner.getWorld(),x,y,z));
                }
            }
        }
        return locationList;
    }
}
