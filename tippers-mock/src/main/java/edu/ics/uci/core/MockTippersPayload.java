package edu.ics.uci.core;

import java.util.Objects;

public class MockTippersPayload {

    private String area;
    private Integer location;
    private Integer floor;
    private String building;

    public MockTippersPayload() {

    }

    public MockTippersPayload(String area, Integer location, Integer floor, String building) {
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

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
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
        MockTippersPayload that = (MockTippersPayload) o;
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
        return "MockTippersPayload{" +
                "area='" + area + '\'' +
                ", location='" + location + '\'' +
                ", floor='" + floor + '\'' +
                ", building='" + building + '\'' +
                '}';
    }
}