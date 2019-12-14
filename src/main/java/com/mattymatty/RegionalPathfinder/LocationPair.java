package com.mattymatty.RegionalPathfinder;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static LocationPair getLocationPair(Location loc, List<Location> locList, Set<Location> ogLocationList) {
        Location maxCorner = getCornerOfPair(loc,true,ogLocationList);
        Location minCorner = getCornerOfPair(loc,false,ogLocationList);
        LocationPair locationPair = new LocationPair(minCorner,maxCorner);
        for(Location location : new ArrayList<>(locList)) {
            if(location.getBlockY() == minCorner.getBlockY()) {
                if(location.getBlockX() >= minCorner.getBlockX() && location.getBlockX() <= maxCorner.getBlockX()) {
                    if(location.getBlockZ() >= minCorner.getBlockZ() && location.getBlockZ() <= maxCorner.getBlockZ()) {
                        locList.remove(location);
                    }
                }
            }
        }
        return locationPair;
    }

    public static Location getCornerOfPair(Location loc, boolean positive, Set<Location> ogLocationList) {
        Location corner = null;
        Location currentLoc = loc.clone();
        boolean keepX = true;
        Vector xVector = new Vector(1,0,0);
        Vector zVector = new Vector(0,0,1);
        if(!positive) {
            xVector.multiply(-1);
            zVector.multiply(-1);
        }
        while(corner == null) {
            if(keepX) {
                Location newLoc = currentLoc.clone().add(xVector);
                if (ogLocationList.contains(newLoc)) {
                    currentLoc = newLoc;
                } else {
                    keepX = false;
                }
            } else {
                Location newLoc = currentLoc.clone().add(zVector);
                if (ogLocationList.contains(newLoc)) {
                    currentLoc = newLoc;
                } else {
                    corner = currentLoc;
                }
            }
        }
        return corner;
    }
}
