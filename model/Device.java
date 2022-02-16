package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("_id")
    private String id;
    private String name;
    private int type;
    private String room;
    private String thumbnail;
//    private int value;
    private String position;
    private String producer;

    public Device(String name, int type, String room, String thumbnail, String position, String producer) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.thumbnail = "thumbnail";
        //this.value = value;
        this.position = position;
        this.producer = producer;
    }
//    public Device(String name, int type, String room, String thumbnail, String position, String producer) {
//        this.name = name;
//        this.type = type;
//        this.room = room;
//        this.thumbnail = "thumbnail";
//        this.position = position;
//        this.producer = producer;
//    }

//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }

    public Device(String id, String name, int type, String room, String thumbnail, String position, String producer) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.thumbnail = thumbnail;
        this.position = position;
        this.producer = producer;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", room='" + room + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", position='" + position + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }
}
