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
        return containsLocation(location,minCorner,maxCorner);
    }

    private static boolean containsLocation(Location checkLocation, Location minCorner, Location maxCorner) {
        if(checkLocation.getBlockY() >= minCorner.getBlockY() && checkLocation.getBlockY() <= maxCorner.getBlockY()) {
            if(checkLocation.getBlockX() >= minCorner.getBlockX() && checkLocation.getBlockX() <= maxCorner.getBlockX()) {
                if(checkLocation.getBlockZ() >= minCorner.getBlockZ() && checkLocation.getBlockZ() <= maxCorner.getBlockZ()) {
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
                    locationList.add(new Location(minCorner.getWorld(),x+0.5,y,z+0.5));
                }
            }
        }
        return locationList;
    }

    public static LocationPair getLocationPair(Location loc, List<Location> locList, Set<Location> ogLocationList) {
        Location maxCorner = getCornerOfPair(loc,true,ogLocationList,locList,null);
        Location minCorner = getCornerOfPair(loc,false,ogLocationList,locList,maxCorner);
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

    public static Location getCornerOfPair(Location loc, boolean positive, Set<Location> ogLocationList, List<Location> changingList, Location setLocation) {
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
                if ((setLocation == null && containsEverything(loc,newLoc,ogLocationList,changingList)) || (setLocation != null && containsEverything(setLocation,newLoc,ogLocationList,changingList))) {
                    currentLoc = newLoc;
                } else {
                    keepX = false;
                }
            } else {
                Location newLoc = currentLoc.clone().add(zVector);
                if ((setLocation == null && containsEverything(loc,newLoc,ogLocationList,changingList)) || (setLocation != null && containsEverything(setLocation,newLoc,ogLocationList,changingList))) {
                    currentLoc = newLoc;
                } else {
                    corner = currentLoc;
                }
            }
        }
        return corner;
    }

    private static boolean containsEverything(Location ogLocation, Location changingLocation, Set<Location> ogLocationList, List<Location> changingList) {
        // We check if the number of missing locations inside the region and the number of actual locations inside match
        int validInside = 0;
        int totalInside;
        for(Location location : ogLocationList) {
            if(containsLocation(location,ogLocation,changingLocation)) {
                if(changingList.contains(location)) {
                    validInside++;
                } else {
                    // If the og list contains it but the changing one doesn't, this place has already been covered.
                    return false;
                }
            }
        }
        totalInside = (Math.abs(ogLocation.getBlockX()-changingLocation.getBlockX())+1) * (Math.abs(ogLocation.getBlockY()-changingLocation.getBlockY())+1) * (Math.abs(ogLocation.getBlockZ()-changingLocation.getBlockZ())+1);
        return validInside == totalInside;
    }
}