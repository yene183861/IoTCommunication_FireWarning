package vn.hust.soict.project.iotcommunication.model;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;

public class User {
    @SerializedName("_id")
    private String id;
    private String name;
    private int deviceType;
    private String deviceId;
    private String versionCode;
    private String deviceName;
    private String fcmToken;
    private String signature;
    private String email;
    private String password;

    public User(String email, String password) {
        this.deviceType = 3;
        this.deviceId = "20183825";
        this.versionCode = "1.0.0";
        this.signature = "1";
        this.email = email;
        this.password = password;
    }


    public User(String id,String name, int deviceType, String deviceId, String versionCode, String deviceName, String fcmToken, String signature, String email, String password) {
        this.name = name;
        this.id = id;
        //this.deviceId = getMacAddr();
        //this.fcmToken = DataLocalManager.getTokenFCM();
        //this.deviceName = getDeviceName();
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.versionCode = versionCode;
        this.deviceName = deviceName;
        this.fcmToken = fcmToken;
        this.signature = signature;
        this.email = email;
        this.password = password;
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

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
//public String getMacAddress(Context context) {
//    WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//    String macAddress = wimanager.getConnectionInfo().getMacAddress();
//    if (macAddress == null) {
//        macAddress = "Device don't have mac address or wi-fi is disabled";
//    }
//    return macAddress;
//}
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "deviceType=" + deviceType +
                ", deviceId='" + deviceId + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", signature=" + signature +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
