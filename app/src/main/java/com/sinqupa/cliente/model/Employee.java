package com.sinqupa.cliente.model;

public class Employee {
    private boolean activated;
    private Double latitudeTravel;
    private Double longitudeTravel;

    public Employee() {
    }

    public Employee(boolean activated, Double latitudeTravel, Double longitudeTravel) {
        this.activated = activated;
        this.latitudeTravel = latitudeTravel;
        this.longitudeTravel = longitudeTravel;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Double getLatitudeTravel() {
        return latitudeTravel;
    }

    public void setLatitudeTravel(Double latitudeTravel) {
        this.latitudeTravel = latitudeTravel;
    }

    public Double getLongitudeTravel() {
        return longitudeTravel;
    }

    public void setLongitudeTravel(Double longitudeTravel) {
        this.longitudeTravel = longitudeTravel;
    }
}
