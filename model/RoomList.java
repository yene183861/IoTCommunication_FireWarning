package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomList {
    @SerializedName("data")
    private List<Room> roomList;

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
