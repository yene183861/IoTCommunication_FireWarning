package vn.hust.soict.project.iotcommunication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    @SerializedName("_id")
    private String id;
    private String device;
private String content;
    @SerializedName("createdAt")
    private long milliseconds;
    private int seen;

    public Notification(String content,String device, int seen) {
        this.device = device;
        this.content = content;
        this.seen = seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", device='" + device + '\'' +
                ", content='" + content + '\'' +
                ", milliseconds=" + milliseconds +
                ", seen=" + seen +
                '}';
    }
}
