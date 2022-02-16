package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    @SerializedName("data")
    private List<Notification> notificationList;

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
