package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeList {
        @SerializedName("data")
        private List<Home> homeList;

    public List<Home> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<Home> homeList) {
        this.homeList = homeList;
    }
}
