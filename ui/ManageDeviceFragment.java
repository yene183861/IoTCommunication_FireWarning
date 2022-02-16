package vn.hust.soict.project.iotcommunication.ui;

import android.content.Context;
import android.os.Bundle;
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


import java.util.List;

//import vn.hust.soict.project.iotcommunication.MyService;
import vn.hust.soict.project.iotcommunication.R;

import vn.hust.soict.project.iotcommunication.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
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
    private Room room;

    public ManageDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

//    private Messenger messenger;
//    private boolean isServiceConnected;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder iBinder) {
//            messenger = new Messenger(iBinder);
//            isServiceConnected = true;
//            sendMessageConnectMQTT();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            messenger = null;
//            isServiceConnected = false;
//        }
//    };

//    private void sendMessageConnectMQTT() {
//        Message message = Message.obtain(null, MyService.MSG_CONNECT_MQTT, 0, 0);
//        try {
//            messenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_list_device, container, false);
        Bundle bundle = this.getArguments();
        room = (Room) bundle.getSerializable("room");

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

    }

//    @Override
//    public void onDestroy() {
//        disconnectMQTT();
//        Log.e("mqtt", "disconnect");
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
        RadioButton radioButtonGas = dialogView.findViewById(R.id.radioButtonGas);
        EditText enterProducer = dialogView.findViewById(R.id.enterProducer);
        TextView btnCreateDevice = dialogView.findViewById(R.id.btnCreateDevice);
        TextView btnCancelDevice = dialogView.findViewById(R.id.btnCancelDevice);
        LinearLayout layout = dialogView.findViewById(R.id.layoutImgDevice);
        layout.setVisibility(View.GONE);
//        //check fields empty
        if (isEdit) {
            addDeviceTitleDialog.setText("Update information Device");
            btnCreateDevice.setText("Update");
            enterDevicePosition.setText(deviceForEdit.getPosition());
            enterNameDevice.setText(deviceForEdit.getName());
            enterProducer.setText(deviceForEdit.getProducer());
            if (deviceForEdit.getType() == 1) {
                radioGroupDeviceType.check(R.id.radioButtonFlame);
            } else {
                radioGroupDeviceType.check(R.id.radioButtonGas);
            }
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

    @Override
    public void onDeviceEditClick(Device device) {
        this.deviceForEdit = device;
        showAddDialog(true);
    }

    @Override
    public void onDeviceDeleteClick(Device device) {
        Log.e("id delete", device.getId());
        deviceListViewModel.deleteDevice(device.getId(), device);

    }

//    public void connectMQTT() {
//        String clientId = MqttClient.generateClientId();
//        client =
//                new MqttAndroidClient(mContext, "tcp://27.72.98.181:3307",
//                        clientId);
//        String topic = "iot_communication";
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//        options.setUserName("USERNAME");
//        options.setPassword("USERNAME".toCharArray());
//
//        try {
//            IMqttToken token = client.connect(options);
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.e("connect mqtt", "onSuccess");
//                    subscribeMqttChannel(topic);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.e("connect mqtt", "onFailure" + exception);
//
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void subscribeMqttChannel(String topicName) {
//        try {
//            Log.d("mqtt", "topic name: " + topicName);
//            Log.d("mqtt", "status connect: " + client.isConnected());
//            if (client.isConnected()) {
//                client.subscribe(topicName, 0);
//                client.setCallback(new MqttCallback() {
//                    @Override
//                    public void connectionLost(Throwable cause) {
//                        Log.e("subscribe mqtt: ", "Connection was lost!");
//                    }
//
//                    @Override
//                    public void messageArrived(String topic, MqttMessage message) throws Exception {
//                        Log.d("subscribe mqtt: ", "message>>" + new String(message.getPayload()));
//                        Log.d("subscribe mqtt: ", "topic>>" + topic);
//                        parseMqttMessage(new String(message.getPayload()));
//
//                    }
//
//                    @Override
//                    public void deliveryComplete(IMqttDeliveryToken token) {
//                        Log.e("subscribe mqtt: ", "Delivery Complete!");
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.d("subscribe mqtt: ", "Error :" + e);
//        }
//    }
//
//    private void parseMqttMessage(String s) {
//        Log.e("message receive", s);
//    }
//
//    private void disconnectMQTT() {
//        if (client.isConnected()) {
//            try {
//                IMqttToken disconToken = client.disconnect();
//                disconToken.setActionCallback(new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        // we are now successfully disconnected
//                        Log.e("mqtt", "disconnect success");
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken,
//                                          Throwable exception) {
//                        Log.e("mqtt", "disconnect failed " + exception);
//                        // something went wrong, but probably we are disconnected anyway
//                    }
//                });
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}





