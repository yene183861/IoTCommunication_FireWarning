package vn.hust.soict.project.iotcommunication.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL_SIGN_UP = "http://27.72.98.181:3303";
    public static String BASE_URL = "http://27.72.98.181:3300";

    public static String BASE_URL_FIREBASE = "https://identitytoolkit.googleapis.com";

    private static Retrofit retrofit;
    private static Retrofit retrofit1;

    public static Retrofit getRetrofitClient(){
        if(retrofit == null){
              retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getRetrofitClient1(){
        if(retrofit1 == null){
            retrofit1 = new Retrofit.Builder().baseUrl(BASE_URL_SIGN_UP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }
}
