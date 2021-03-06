package vn.hust.soict.project.iotcommunication.data_local;

import android.content.Context;

public class DataLocalManager {
    private static final String TOKEN_SERVER = "TOKEN_SERVER";
    private static final String CLIENT_ID = "CLIENT_ID";
    private static final String FCM_TOKEN = "FCM_TOKEN";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setTokenServer(String tokenServer){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(TOKEN_SERVER,tokenServer);
    }
    public static String getTokenServer(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(TOKEN_SERVER);
    }
    public static void setClientId(String id){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(CLIENT_ID,id);
    }
    public static String getClientId(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(CLIENT_ID);
    }
    public static void setFCMToken(String token){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(FCM_TOKEN,token);
    }
    public static String getFCMToken(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(FCM_TOKEN);
    }
}
