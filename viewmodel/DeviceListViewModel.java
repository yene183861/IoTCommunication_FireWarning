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
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.Room;

public class DeviceListViewModel extends ViewModel {
    private List<Device> mListDevice;
    private MutableLiveData<List<Device>> deviceListLiveData;
    private Room room;

    //ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);

    public DeviceListViewModel() {
        deviceListLiveData = new MutableLiveData<>();
            mListDevice = new ArrayList<>();
//            deviceListLiveData.setValue(mListDevice);
    }

    public void getDeviceList(String id) {
//        Log.e("callApiGetRoomList", "" + id);
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<DeviceList> call = apiService.getDeviceList(DataLocalManager.getTokenServer(), id);
        call.enqueue(new Callback<DeviceList>() {
            @Override
            public void onResponse(Call<DeviceList> call, Response<DeviceList> response) {
                if (response.code() == 200) {
                    mListDevice = new ArrayList<>(response.body().getDeviceList());
                    deviceListLiveData.setValue(mListDevice);
                } else {
                    try {
                        Log.e("callApiGetDeviceList", "error code" + response.code()+ " " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("callApiGetDeviceList", "error" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceList> call, Throwable t) {
                Log.e("callApiGetDeviceList", "call api failed " + t);
            };

        });
    }

    public MutableLiveData<List<Device>> getDevicesListObserver() {
        return deviceListLiveData;
    }

    public void insertDevice(Device device) {
            ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
            Call<Device> call = apiService.insertDevice(DataLocalManager.getTokenServer(),device);
            call.enqueue(new Callback<Device>() {
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.code() == 201) {
                        mListDevice.add(device);
                        deviceListLiveData.setValue(mListDevice);
                        Log.e("insertDevice", "insert success");
                    } else {
                        try {
                            Log.e("insertDevice", "error code: " + response.code() + "error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("insertDevice", "error: " + e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    Log.e("insertDevice", "add Device failed");
                }
            });

    }

    public void updateDevice(String id, Device device) {
            ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
            Call<Device> call = apiService.updateDevice(DataLocalManager.getTokenServer(), id, device);
            call.enqueue(new Callback<Device>() {
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.code() == 201) {
                        deviceListLiveData.setValue(mListDevice);
                        Log.e("updateDevice", "update success");
                    } else {
                        try {
//                            if(response.body() != null) {
//                                String s = response.body().string();
//                            }
                            Log.e("updateDevice", "error code: " + response.code() + "error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("updateDevice", "error: " + e);
                        }
                    }
//                    deviceListLiveData.setValue(mListDevice);
//                homeListLiveData.setValue(mListHome);
                }

                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    Log.e("updateDevice", "update Device failed");
                }
            });
    }

    public void deleteDevice(String id, Device device) {
            ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
            Call<Void> call = apiService.deleteDevice(DataLocalManager.getTokenServer(),id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        mListDevice.remove(device);
                        deviceListLiveData.setValue(mListDevice);
                        Log.e("deleteDevice", "delete success");
                    } else {
                        try {
                            Log.e("deleteDevice", "error code: " + response.code() + "error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("deleteDevice", "error: " + e);
                        }
                    }
//                    mListDevice.remove(device);
////                deviceListLiveData.setValue(mListDevice);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("deleteDevice", "delete device failed " + t);
                }
            });
    }
}

