package vn.hust.soict.project.iotcommunication.model;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Room implements Serializable {
    @SerializedName("home")
    private String homeId;
    @SerializedName("_id")
    private String id;
    private int type;
    private int floor;
    private int area;
    private String position;
    private String owner;
    private String thumbnail;

    public Room(String homeId, String id, int type, int floor, int area, String position, String owner, String thumbnail) {
        this.homeId = homeId;
        this.id = id;
        this.type = type;
        this.floor = floor;
        this.area = area;
        this.position = position;
        this.owner = owner;
        this.thumbnail = "thumbnail";
    }

    public Room(String homeId, int type, int floor, int area, String position, String owner, String thumbnail) {
        this.homeId = homeId;
        this.type = type;
        this.floor = floor;
        this.area = area;
        this.position = position;
        this.owner = owner;
        this.thumbnail = "thumbnail";
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
