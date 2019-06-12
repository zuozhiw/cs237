package edu.ics.uci.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildingLocations {
    public static Map<String, String> buildingLocations = new HashMap<>();
    public static void init(){
        buildingLocations.put("DBH", "-117.841989,33.643161");
        buildingLocations.put("PCB", "-117.843356,33.643414");
        buildingLocations.put("ARC", "-117.828206,33.643422");
        buildingLocations.put("ICS", "-117.841897,33.644132");
        buildingLocations.put("SH", "-117.844725,33.646131");
    }
    public static List<Double> getBuildingLocation(String building){
        String locationString;
        if (BuildingLocations.buildingLocations.containsKey(building)){
            locationString = BuildingLocations.buildingLocations.get(building);
        }else{
            locationString = "-117.841989,33.643161";
        }
        List<Double> userCoordinates = Arrays.stream(locationString.split(",")).map(s -> Double.parseDouble(s)).collect(Collectors.toList());
        return userCoordinates;
    }
}
