package com.sinqupa.cliente.model;

public class Alarm {
    private Integer distance;
    private boolean actived;
    private String sound;
    private String uri;
    private String latitude;
    private String longitude;

    public Alarm() {
    }

    public Alarm(Integer distance, boolean actived, String sound, String uri, String latitude, String longitude) {

        this.distance = distance;
        this.actived = actived;
        this.sound = sound;
        this.uri = uri;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public boolean getActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
