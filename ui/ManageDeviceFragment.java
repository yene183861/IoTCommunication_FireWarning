package vn.hust.soict.project.iotcommunication.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

import vn.hust.soict.project.iotcommunication.MyService;
import vn.hust.soict.project.iotcommunication.R;

import vn.hust.soict.project.iotcommunication.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.Room;
import vn.hust.soict.project.iotcommunication.model.Device;
import vn.hust.soict.project.iotcommunication.viewmodel.DeviceListViewModel;

public class ManageDeviceFragment extends Fragment implements DeviceListAdapter.ItemClickListener {
    private ImageView btnAddNewDevice;
    private TextView tvNoDeviceList;
    private RecyclerView recyclerViewDevice;
    private DeviceListViewModel deviceListViewModel;
    private Context mContext;
    private List<Device> deviceList;
    private DeviceListAdapter adapter;
    private Device deviceForEdit;
    ImageView deviceThumbnail;
    Room room;

    public ManageDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private Messenger messenger;
    private boolean isServiceConnected;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            isServiceConnected = true;
            sendMessageConnectMQTT();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            isServiceConnected = false;
        }
    };

    private void sendMessageConnectMQTT() {
        Message message = Message.obtain(null, MyService.MSG_CONNECT_MQTT, 0, 0);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_list_device, container, false);
        Bundle bundle = this.getArguments();
        room = (Room) bundle.getSerializable("room");
        //service
        Intent intent = new Intent(mContext, MyService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        btnAddNewDevice = view.findViewById(R.id.btnAddNewDevice);
        recyclerViewDevice = view.findViewById(R.id.recyclerViewDevice);
        tvNoDeviceList = view.findViewById(R.id.tvNoDeviceList);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerViewDevice.setLayoutManager(linearLayoutManager);
        adapter = new DeviceListAdapter(mContext, deviceList, this);
        recyclerViewDevice.setAdapter(adapter);

        deviceListViewModel = new ViewModelProvider(this).get(DeviceListViewModel.class);
        deviceListViewModel.getDevicesListObserver().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                if (devices == null) {
                    recyclerViewDevice.setVisibility(View.GONE);
                    tvNoDeviceList.setVisibility(View.VISIBLE);
                } else {
                    deviceList = devices;
                    adapter.setDeviceList(devices);
                    tvNoDeviceList.setVisibility(View.GONE);
                    recyclerViewDevice.setVisibility(View.VISIBLE);
                }
            }
        });
        deviceListViewModel.getDeviceList(room.getId());

        btnAddNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);

            }
        });
        return view;
//        getActivity().unbindService(serviceConnection);
    }

    @Override
    public void onPause() {
        getActivity().unbindService(serviceConnection);
        Log.e("log", "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("log", "onResume");
        Intent intent = new Intent(mContext, MyService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

//    @Override
//    public void onDestroy() {
//        getActivity().unbindService(serviceConnection);
//        Log.e("log", "onDestroy");
//        super.onDestroy();
//    }

    private void showAddDialog(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_device, null);
        TextView addDeviceTitleDialog = dialogView.findViewById(R.id.addDeviceTitleDialog);
        EditText enterNameDevice = dialogView.findViewById(R.id.enterNameDevice);
        EditText enterDevicePosition = dialogView.findViewById(R.id.enterDevicePosition);
        RadioGroup radioGroupDeviceType = dialogView.findViewById(R.id.radioGroupDeviceType);
        RadioButton radioButtonFlame = dialogView.findViewById(R.id.radioButtonFlame);
//        RadioButton radioButtonTemperature = dialogView.findViewById(R.id.radioButtonTemperature);
        RadioButton radioButtonGas = dialogView.findViewById(R.id.radioButtonGas);
        EditText enterProducer = dialogView.findViewById(R.id.enterProducer);
        TextView btnCreateDevice = dialogView.findViewById(R.id.btnCreateDevice);
        TextView btnCancelDevice = dialogView.findViewById(R.id.btnCancelDevice);
        LinearLayout layout = dialogView.findViewById(R.id.layoutImgDevice);
//        int deviceType = radioGroupDeviceType.getCheckedRadioButtonId();
//        int type = 0;
//
//        //check fields empty
        if (isEdit) {
            addDeviceTitleDialog.setText("Update information Device");
            btnCreateDevice.setText("Update");
            enterDevicePosition.setText(deviceForEdit.getPosition());
            enterNameDevice.setText(deviceForEdit.getName());
            enterProducer.setText(deviceForEdit.getProducer());


//            enterAreaHome.setText(String.valueOf(deviceForEdit.getArea()));
//            enterRoom.setText(String.valueOf(deviceForEdit.getRoom()));
//            enterFloor.setText(String.valueOf(deviceForEdit.getFloor()));
//            enterMembers.setText(String.valueOf(deviceForEdit.getMembers()));
//                try {
//                    imageHome.setImageBitmap(MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), homeForEdit.getImage()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        }
        btnCancelDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterNameDevice.getText().toString().trim();
                String position = enterDevicePosition.getText().toString().trim();
                String producer = enterProducer.getText().toString().trim();
//                int area = Integer.parseInt(enterRoomArea.getText().toString().trim());
//                int room = Integer.parseInt(enterRoom.getText().toString().trim());
//                int floor = Integer.parseInt(enterFloorRoom.getText().toString().trim());
//                int members = Integer.parseInt(enterMembers.getText().toString().trim());
                int roomType = radioGroupDeviceType.getCheckedRadioButtonId();

                int type = 1;
                if (roomType == R.id.radioButtonFlame) {
                    type = 1;
                } else if (roomType == R.id.radioButtonGas) {
                    type = 2;
                }

                //check fields empty
                //call view model
                if (isEdit) {
                    deviceForEdit.setName(name);
                    deviceForEdit.setPosition(position);
                    deviceForEdit.setProducer(producer);
                    deviceForEdit.setType(type);
//                    deviceForEdit.setFloor(floor);
//                    deviceForEdit.setMembers(members);
//                    deviceForEdit.setImage(img);
                    deviceListViewModel.updateDevice(deviceForEdit.getId(), deviceForEdit);
                } else {
                    //call view model
                    Device device = new Device(name, type, room.getId(), "null", position, producer);
                    deviceListViewModel.insertDevice(device);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

//    @Override
//    public void onRoomClick(Room room) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ManageSensorFragment fragment = new ManageSensorFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("room", room);
//        fragment.setArguments(bundle);
//        transaction.replace(R.id.contentFrame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    @Override
    public void onDeviceEditClick(Device device) {
        this.deviceForEdit = device;
        showAddDialog(true);
////        home = this.homeForEdit;
//        homeListViewModel.updateHome(homeForEdit);
    }

    @Override
    public void onDeviceDeleteClick(Device device) {
        deviceListViewModel.deleteDevice(device.getId(), device);

    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

//    private void connectMQTT() {
//        String clientId = MqttClient.generateClientId();
//        MqttAndroidClient client =
//                new MqttAndroidClient(this.getActivity().getApplicationContext(), "27.72.98.181:3307",
//                        clientId);
////
////        try {
//            IMqttToken token = client.connect();
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.d("connect MQTT", "onSuccess");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d("connect MQTT", "onFailure");
//
//                }
////            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

}





