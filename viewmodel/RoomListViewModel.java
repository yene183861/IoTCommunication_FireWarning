package vn.hust.soict.project.iotcommunication.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotcommunication.api.ApiService;
import vn.hust.soict.project.iotcommunication.api.RetrofitInstance;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.HomeList;
import vn.hust.soict.project.iotcommunication.model.Room;
import vn.hust.soict.project.iotcommunication.model.RoomList;

public class RoomListViewModel extends ViewModel {
    private List<Room> mListRoom;
    private MutableLiveData<List<Room>> roomListLiveData;
    private Home home;

    public RoomListViewModel() {
        roomListLiveData = new MutableLiveData<>();
        mListRoom = new ArrayList<>();
//        roomListLiveData.setValue(mListRoom);
    }

    public void getRoomList(String id) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<RoomList> call = apiService.getRoomList(DataLocalManager.getTokenServer(), id);
        call.enqueue(new Callback<RoomList>() {
            @Override
            public void onResponse(Call<RoomList> call, Response<RoomList> response) {
                if (response.code() == 200) {
                    mListRoom = new ArrayList<>(response.body().getRoomList());
                    roomListLiveData.setValue(mListRoom);
                } else {
                    try {
                        Log.e("callApiGetRoomList", "error code " + response.code() + " " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("callApiGetRoomList", "error" + e);
                    }
                }

            }

            @Override
            public void onFailure(Call<RoomList> call, Throwable t) {
                Log.e("callApiGetRoomList", "call api failed " + t);
            }
        });
    }

    public MutableLiveData<List<Room>> getRoomsListObserver() {
        return roomListLiveData;
    }

    public void insertRoom(Room room) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Room> call = apiService.insertRoom(DataLocalManager.getTokenServer(), room);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.code() == 201) {
                    mListRoom.add(room);
                    roomListLiveData.setValue(mListRoom);
                    Log.e("insertRoom", "insert success");
                } else {
                    try {
                        Log.e("insertRoom", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("insertRoom", "error: " + e);
                    }
                }
//
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.e("insertRoom", "insert room failed " + t);
            }
        });
//        mListRoom.add(room);
//        roomListLiveData.setValue(mListRoom);
    }

    public void updateRoom(String id, Room room) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Room> call = apiService.updateRoom(DataLocalManager.getTokenServer(), id, room);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.code() == 200) {
                    roomListLiveData.setValue(mListRoom);
                    Log.e("updateRoom", "update success");
                } else {
                    try {
                        Log.e("updateRoom", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("updateRoom", "error: " + e);
                    }
                }
//
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.e("updateRoom", "update room failed " + t);
            }
        });
        roomListLiveData.setValue(mListRoom);
    }

    public void deleteRoom(String id, Room room) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Void> call = apiService.deleteRoom(DataLocalManager.getTokenServer(), id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    mListRoom.remove(room);
                    roomListLiveData.setValue(mListRoom);
                    Log.e("deleteRoom", "delete success");
                } else {
                    try {
                        Log.e("deleteRoom", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("deleteRoom", "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("deleteRoom", "delete room failed " + t);
            }
        });
    }
}
