package vn.hust.soict.project.iotcommunication.api;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.hust.soict.project.iotcommunication.model.DeviceList;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.HomeList;
import vn.hust.soict.project.iotcommunication.model.Notification;
import vn.hust.soict.project.iotcommunication.model.NotificationResponse;
import vn.hust.soict.project.iotcommunication.model.Room;
import vn.hust.soict.project.iotcommunication.model.RoomList;
import vn.hust.soict.project.iotcommunication.model.Device;
import vn.hust.soict.project.iotcommunication.model.User;
import vn.hust.soict.project.iotcommunication.model.UserResponse;

public interface ApiService {
  //  String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MjAzMmQyMDc4NmZmNzAwMWEwYmM2ZDIiLCJ1aWQiOiI2MjAzMmQyMDc4NmZmNzAwMWEwYmM2ZDIiLCJkZXZpY2VJZCI6IjIwMTgzODI1IiwibmFtZSI6IkTGsMahbmcgS2jhuq9jIFThuqFvIiwidmVyc2lvbkNvZGUiOiIxLjAuMCIsImRldmljZVR5cGUiOjMsImxvZ2luVHlwZSI6MSwiaWF0IjoxNjQ0NTU5NjM4LCJleHAiOjQ4MDAzMTk2Mzh9.fyd--1X9uaJYfJPkPDIiAz-C3LLtmaGKNK2Q8KYoaBg";
//    @FormUrlEncoded
//    @POST("/v1/accounts:signInWithPassword")
//Call<JsonObject> getTokenSignIn(@Query("key") String key, @Field("email") String email, @Field("password") String password, @Field("returnSecureToken") boolean returnSecureToken);

    @POST("/signup")
    Call<UserResponse> signUp(@Body User user);

    @POST("/loginEmail")
    Call<UserResponse> loginEmail(@Body User user);


//api CRUD home
    @GET("/home")
    Call<HomeList> getHomeList(@Header("x-access-token") String token);

    @POST("/home")
    Call<Home> insertHome(@Header("x-access-token") String token,@Body Home home);

    @PUT("/home/{id}")
    Call<Home> updateHome(@Header("x-access-token") String token,@Path("id") String id, @Body Home home);

    @DELETE("/home/{id}")
    Call<Void> deleteHome(@Header("x-access-token") String token,@Path("id") String id);

    @POST("/upload")
    Call<Home> upload(@Header("x-access-token") String token, @Body Home home);

    //api CRUD room

    @GET("/room")
    Call<RoomList> getRoomList(@Header("x-access-token") String token, @Query("home") String id);


    @POST("/room")
    Call<Room> insertRoom(@Header("x-access-token") String token, @Body Room room);


    @PUT("/room/{id}")
    Call<Room> updateRoom(@Header("x-access-token") String token,@Path("id") String id, @Body Room room);


    @DELETE("/room/{id}")
    Call<Void> deleteRoom(@Header("x-access-token") String token,@Path("id") String id);

    ////api CRUD device
    @GET("/device")
    Call<DeviceList> getDeviceList(@Header("x-access-token") String token, @Query("room") String id);

    @POST("/device")
    Call<Device> insertDevice(@Header("x-access-token") String token, @Body Device device);

    @PUT("/device/{id}")
    Call<Device> updateDevice(@Header("x-access-token") String token, @Path("id") String id, @Body Device device);

    @DELETE("/device/{id}")
    Call<Void> deleteDevice(@Header("x-access-token") String token, @Path("id") String id);

    @GET("/warning")
    Call<NotificationResponse> getNotificationList(@Header("x-access-token") String token);

}
