package edu.ics.uci.core;

import java.util.Objects;

public class TippersPayload {

    private String area;
    private String location;
    private String floor;
    private String building;

    public TippersPayload() {

    }

    public TippersPayload(String area, String location, String floor, String building) {
        this.area = area;
        this.location = location;
        this.floor = floor;
        this.building = building;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TippersPayload that = (TippersPayload) o;
        return Objects.equals(area, that.area) &&
                Objects.equals(location, that.location) &&
                Objects.equals(floor, that.floor) &&
                Objects.equals(building, that.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, location, floor, building);
    }

    @Override
    public String toString() {
        return "TippersPayload{" +
                "area='" + area + '\'' +
                ", location='" + location + '\'' +
                ", floor='" + floor + '\'' +
                ", building='" + building + '\'' +
                '}';
    }
}
