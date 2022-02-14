package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceList {
    @SerializedName("data")
    private List<Device> deviceList;

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
