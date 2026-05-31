package io.hospital.model;

import io.hospital.enums.WardType;
import org.springframework.data.annotation.Id;

public class Ward {
    @Id
    private String id;
    private String name;
    private WardType wardType;
    private int totalBeds;
    private int currentOccupancy;

    public Ward() {
        //No argument constructor
    }

    public Ward(String name, WardType wardType, int totalBeds) {
        this.name = name;
        this.wardType = wardType;
        this.totalBeds = totalBeds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WardType getWardType() {
        return wardType;
    }

    public void setWardType(WardType wardType) {
        this.wardType = wardType;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(int totalBeds) {
        this.totalBeds = totalBeds;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public int getOccupancyPercent() {
        return (currentOccupancy * 100) / this.totalBeds;
    }

    public boolean hasAvailableBeds() {
        return currentOccupancy < totalBeds;
    }

}
