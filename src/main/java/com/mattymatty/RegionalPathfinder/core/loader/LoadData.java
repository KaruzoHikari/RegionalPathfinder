package com.mattymatty.RegionalPathfinder.core.loader;

import com.mattymatty.RegionalPathfinder.LocationPair;
import com.mattymatty.RegionalPathfinder.RegionalPathfinder;
import com.mattymatty.RegionalPathfinder.core.graph.Edge;
import com.mattymatty.RegionalPathfinder.core.graph.Node;
import com.mattymatty.RegionalPathfinder.core.region.BaseRegionImpl;
import org.bukkit.Location;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadData {
    public final WeakReference<BaseRegionImpl> region;
    //data from Region
    public Location upperCorner;
    public Location lowerCorner;
    public Location samplePoint;
    //generated datas;
    Graph<Node, Edge> graph;
    Graph<Node, Edge> reachableGraph;
    ShortestPathAlgorithm<Node, Edge> shortestPath;
    Map<Integer, Node> nodesMap;
    Status status;
    int x_size;
    int y_size;
    int z_size;
    public final Map<Integer, Map<Integer, Map<Integer, Location>>> reachableLocationsMap = new HashMap<>();
    public final Map<Integer, List<LocationPair>> reachableLocations = new HashMap<>();

    public LoadData(BaseRegionImpl region, Location upperCorner, Location lowerCorner) {
        this.region = new WeakReference<>(region);
        this.upperCorner = upperCorner;
        this.lowerCorner = lowerCorner;
        this.x_size = upperCorner.getBlockX() - lowerCorner.getBlockX();
        this.y_size = upperCorner.getBlockY() - lowerCorner.getBlockY();
        this.z_size = upperCorner.getBlockZ() - lowerCorner.getBlockZ();
        this.status = null;
        this.nodesMap = new HashMap<>();
    }

    public BaseRegionImpl getRegion() {
        if (region.get() == null)
            throw new RuntimeException("Region has been Garbage Collected");
        return region.get();
    }

    public Status getStatus() {
        return status;
    }

    public int getX_size() {
        return x_size;
    }

    public int getY_size() {
        return y_size;
    }

    public int getZ_size() {
        return z_size;
    }

    public Node getNode(Location loc) {
        return RegionalPathfinder.getInstance().nodeMap.get(loc);
    }

    public Map<Location, Node> getNodesMap() {
        return RegionalPathfinder.getInstance().nodeMap;
    }

    public Graph<Node, Edge> getGraph() {
        return graph;
    }

    public Graph<Node, Edge> getReachableGraph() {
        return reachableGraph;
    }


    public ShortestPathAlgorithm<Node, Edge> getShortestPath() {
        return shortestPath;
    }

    public enum Status implements Comparable<Status> {
        LOADING(0),
        LOADED(1),
        EVALUATING(2),
        EVALUATED(3),
        VALIDATING(4),
        VALIDATED(5);

        final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void invalidate() {
        if (status == Status.VALIDATED)
            status = Status.EVALUATED;
    }

    public void delete() {
        this.graph = null;
        this.reachableGraph = null;
        this.region.clear();
        this.reachableLocationsMap.clear();
        this.upperCorner = null;
        this.lowerCorner = null;
        this.samplePoint = null;
        this.status = null;
        this.shortestPath = null;
        this.nodesMap.clear();
        this.nodesMap = null;
    }

    public List<LocationPair> getLocationPairs() {
        List<LocationPair> locationPairList = new ArrayList<>();
        for(List<LocationPair> locList : reachableLocations.values()) {
            locationPairList.addAll(locList);
        }
        return locationPairList;
    }

    public List<LocationPair> getLocationPairs(int y) {
        if(reachableLocationsMap.containsKey(y)) {
            return reachableLocations.get(y);
        }
        return new ArrayList<>();
    }

    public Map<Integer, List<LocationPair>> getLocationPairsMap() {
        return reachableLocations;
    }
}
