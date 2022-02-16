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
import vn.hust.soict.project.iotcommunication.model.Device;
import vn.hust.soict.project.iotcommunication.model.DeviceList;
import vn.hust.soict.project.iotcommunication.model.Notification;
import vn.hust.soict.project.iotcommunication.model.NotificationResponse;
import vn.hust.soict.project.iotcommunication.model.Room;

public class NotificationListViewModel extends ViewModel {
    private List<Notification> mlist;
    private MutableLiveData<List<Notification>> listLiveData;

    public NotificationListViewModel() {
        listLiveData = new MutableLiveData<>();
        mlist = new ArrayList<>();
    }

    public void getNotificationList() {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<NotificationResponse> call = apiService.getNotificationList(DataLocalManager.getTokenServer());
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.code() == 200) {
                    mlist = response.body().getNotificationList();
                    listLiveData.setValue(mlist);
                } else {
                    try {
                        Log.e("Notification", "error code" + response.code()+ " " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Notification", "error" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.e("Notification", "call api failed " + t);
            };

        });
    }

    public MutableLiveData<List<Notification>> getNotificationsListObserver() {
        return listLiveData;
    }

}

