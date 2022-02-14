package vn.hust.soict.project.iotcommunication.model;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Home implements Serializable {
    @SerializedName("_id")
    private String id;
    private String user;
    private String name;
    private String address;
    private int area;
//private int area;
    private int rooms;
    private int floor;
//    private int members;
private int members;
    private String thumbnail;

    public Home( String name, String address, int area, int rooms, int floor, int members, String thumbnail) {
        this.name = name;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.floor = floor;
        this.members = members;
        this.thumbnail = "thumbnail";
    }

    public Home( String id, String user, String name, String address, int area, int rooms, int floor, int members, String thumbnail) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.floor = floor;
        this.members = members;
        this.thumbnail = "thumbnail";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    //
//    public int getArea() {
//        return area;
//    }
//
//    public void setArea(int area) {
//        this.area = area;
//    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

//    public int getMembers() {
//        return members;
//    }
//
//    public void setMembers(int members) {
//        this.members = members;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Home{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", rooms=" + rooms +
                ", floor=" + floor +
                ", members=" + members +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
