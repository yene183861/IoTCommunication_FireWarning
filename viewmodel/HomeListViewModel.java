package vn.hust.soict.project.iotcommunication.viewmodel;

import android.util.Log;
import android.widget.Toast;

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

public class HomeListViewModel extends ViewModel {
    private MutableLiveData<List<Home>> homeListLiveData;
    private List<Home> mListHome;
    private HomeList homeListModel;

    public HomeListViewModel() {
        homeListLiveData = new MutableLiveData<>();
//        homeListModel = new HomeList();
        mListHome = new ArrayList<>();
//        homeListLiveData.setValue(mListHome);
    }

    public void getHomeList() {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<HomeList> call = apiService.getHomeList(DataLocalManager.getTokenServer());
        call.enqueue(new Callback<HomeList>() {
            @Override
            public void onResponse(Call<HomeList> call, Response<HomeList> response) {//response.code() == 200
                if (response.code() == 200) {
                    mListHome = new ArrayList<>(response.body().getHomeList());
                    homeListLiveData.setValue(mListHome);
                } else {
                    try {
                        Log.e("callApiGetHomeList", "error body: " + response.code() + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("callApiGetHomeList", "error: " + e);
                    }
                }

            }

            @Override
            public void onFailure(Call<HomeList> call, Throwable t) {
                Log.e("callApiGetHomeList", "call api failed " + t);
            }
        });
    }

    public MutableLiveData<List<Home>> getHomesListObserver() {
        return homeListLiveData;
    }

    public void insertHome(Home home) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Home> call = apiService.insertHome(DataLocalManager.getTokenServer(), home);
        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.code() == 201) {
//                    if (mListHome == null) {
//                        mListHome = new ArrayList<>();
//                    }
                    mListHome.add(home);
                    homeListLiveData.setValue(mListHome);
                    Log.e("insertHome", "insert success");
                } else {
                    try {
                        Log.e("insertHome", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("insertHome", "call api failed: " + e);
                    }
                }
//
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.e("insertHome", "insert home failed " + t);
            }
        });
    }

    public void updateHome(String id, Home home) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Home> call = apiService.updateHome(DataLocalManager.getTokenServer(), id, home);
        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.code() == 200) {
                    homeListLiveData.setValue(mListHome);
                    Log.e("updateHome", "update success");
                } else {
                    try {
                        Log.e("updateHome", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("updateHome", "error: " + e);
                    }
                }
//
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.e("updateHome", "call api failed: " + t);
            }
        });
        homeListLiveData.setValue(mListHome);
    }

    public void deleteHome(String id, Home home) {
        ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
        Call<Void> call = apiService.deleteHome(DataLocalManager.getTokenServer(), id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    mListHome.remove(home);
                    homeListLiveData.setValue(mListHome);
                }else {
                    try {
                        Log.e("deleteHome", "error code: " + response.code() + "error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("deleteHome", "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("deleteHome", "call api failed: " + t);
            }
        });
    }
        }
