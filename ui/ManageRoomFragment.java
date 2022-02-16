package vn.hust.soict.project.iotcommunication.ui;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.adapter.HomeListAdapter;
import vn.hust.soict.project.iotcommunication.adapter.RoomListAdapter;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.Room;
import vn.hust.soict.project.iotcommunication.viewmodel.HomeListViewModel;
import vn.hust.soict.project.iotcommunication.viewmodel.RoomListViewModel;

public class ManageRoomFragment extends Fragment implements RoomListAdapter.ItemClickListener {
    private ImageView btnAddNewRoom;
    private TextView tvNoRoomList;
    private RecyclerView recyclerViewRoom;
    private RoomListViewModel roomListViewModel;
    private Context mContext;
    private List<Room> roomList;
    private RoomListAdapter adapter;
    private Room roomForEdit;
    ImageView roomThumbnail;
    Uri img = null;
    Home home;

    public ManageRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_list_room, container, false);
        Bundle bundle = this.getArguments();
        home = (Home) bundle.getSerializable("home");
        btnAddNewRoom = view.findViewById(R.id.btnAddNewRoom);
        recyclerViewRoom = view.findViewById(R.id.recyclerViewRoom);
        tvNoRoomList = view.findViewById(R.id.tvNoRoomList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewRoom.setLayoutManager(linearLayoutManager);
        adapter = new RoomListAdapter(mContext, roomList, this);
        recyclerViewRoom.setAdapter(adapter);

        roomListViewModel = new ViewModelProvider(this).get(RoomListViewModel.class);
        roomListViewModel.getRoomsListObserver().observe(getViewLifecycleOwner(), new Observer<List<Room>>() {
            @Override
            public void onChanged(List<Room> rooms) {
                if (rooms == null) {
                    recyclerViewRoom.setVisibility(View.GONE);
                    tvNoRoomList.setVisibility(View.VISIBLE);
                } else {
                    roomList = rooms;
                    adapter.setRoomList(rooms);
                    //recyclerViewRoom.setAdapter(adapter);
                    tvNoRoomList.setVisibility(View.GONE);
                    recyclerViewRoom.setVisibility(View.VISIBLE);
                }

            }
        });
        roomListViewModel.getRoomList(home.getId());

        btnAddNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
        return view;
    }

    private void showAddDialog(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_room, null);
        TextView titleDialogRoom = dialogView.findViewById(R.id.titleDialogRoom);
        EditText enterOwner = dialogView.findViewById(R.id.enterOwner);
        EditText enterRoomPosition = dialogView.findViewById(R.id.enterRoomPosition);
        EditText enterRoomArea = dialogView.findViewById(R.id.enterRoomArea);
        EditText enterFloorRoom = dialogView.findViewById(R.id.enterFloorRoom);


        RadioGroup radioGroupRoomType = dialogView.findViewById(R.id.radioGroupRoomType);
        RadioButton radioButtonLivingRoom = dialogView.findViewById(R.id.radioButtonLivingRoom);
        RadioButton radioButtonBedroom = dialogView.findViewById(R.id.radioButtonBedroom);
        RadioButton radioButtonKitchen = dialogView.findViewById(R.id.radioButtonKitchen);
        RadioButton radioButtonDinnerRoom = dialogView.findViewById(R.id.radioButtonDinnerRoom);
        RadioButton radioButtonBathroom = dialogView.findViewById(R.id.radioButtonBathroom);


        TextView btnCreateRoom = dialogView.findViewById(R.id.btnCreateRoom);
        TextView btnCancelRoom = dialogView.findViewById(R.id.btnCancelRoom);
        ImageView chooseRoomThumbnail = dialogView.findViewById(R.id.chooseRoomThumbnail);
        roomThumbnail = dialogView.findViewById(R.id.roomThumbnail);
        LinearLayout layoutImgRoom = dialogView.findViewById(R.id.layoutImgRoom);
        layoutImgRoom.setVisibility(View.GONE);

        if (isEdit) {
            titleDialogRoom.setText("Update information room");
            btnCreateRoom.setText("Update");
            enterOwner.setText(roomForEdit.getOwner());
            enterRoomPosition.setText(roomForEdit.getPosition());
            enterRoomArea.setText(String.valueOf(roomForEdit.getArea()));
            enterFloorRoom.setText(String.valueOf(roomForEdit.getFloor()));
            switch (roomForEdit.getType()){
                case 1: radioGroupRoomType.check(R.id.radioButtonLivingRoom); break;
                case 2: radioGroupRoomType.check(R.id.radioButtonBedroom); break;
                case 3: radioGroupRoomType.check(R.id.radioButtonKitchen); break;
                case 4: radioGroupRoomType.check(R.id.radioButtonDinnerRoom); break;
                case 5: radioGroupRoomType.check(R.id.radioButtonBathroom); break;
            }
//            enterMembers.setText(String.valueOf(roomForEdit.getMembers()));
            //layoutImgRoom.setVisibility(View.GONE);
//                try {
//                    imageHome.setImageBitmap(MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), homeForEdit.getImage()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        }
//        radioGroupRoomType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                doOnDifficultyLevelChanged(group, checkedId);
//            }
//        });
        chooseRoomThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        btnCancelRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String owner = enterOwner.getText().toString().trim();
                String position = enterRoomPosition.getText().toString().trim();
                int area = Integer.parseInt(enterRoomArea.getText().toString().trim());
                int floor = Integer.parseInt(enterFloorRoom.getText().toString().trim());
                int roomType = radioGroupRoomType.getCheckedRadioButtonId();
                Log.e("room", " " + roomType);
                int type = 1;

                //check fields empty
                //call view model
                if (roomType == R.id.radioButtonLivingRoom) {
                    Log.e(" living room", " " + R.id.radioButtonLivingRoom);
                    type = 1;
                } else if (roomType == R.id.radioButtonBedroom) {
                    Log.e("radioButtonBedroom", " " + R.id.radioButtonBedroom);

                    type = 2;
                } else if (roomType == R.id.radioButtonKitchen) {
                    Log.e("radioButtonKitchen", " " + R.id.radioButtonKitchen);

                    type = 3;
                } else if (roomType == R.id.radioButtonDinnerRoom) {
                    Log.e(" radioButtonDinnerRoom", " " + R.id.radioButtonDinnerRoom);

                    type = 4;
                } else if (roomType == R.id.radioButtonBathroom) {
                    Log.e("Bathroom", " " + R.id.radioButtonBathroom);

                    type = 5;
                }
                if (isEdit) {
                    roomForEdit.setOwner(owner);
                    roomForEdit.setType(type);
                    roomForEdit.setPosition(position);
                    roomForEdit.setArea(area);
                    roomForEdit.setFloor(floor);
                    roomListViewModel.updateRoom(roomForEdit.getId(), roomForEdit);
                } else {
                    Log.e("type", " " + type);
                    //call view model
                    Room room = new Room(home.getId(), type, floor, area, position, owner, "null");
                    roomListViewModel.insertRoom(room);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void requestPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        TedBottomPicker.with((FragmentActivity) mContext)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                            roomThumbnail.setImageBitmap(bitmap);
                            roomThumbnail.setVisibility(View.VISIBLE);
                            img = getImageUri(mContext, bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRoomClick(Room room) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ManageDeviceFragment fragment = new ManageDeviceFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("room", room);
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRoomEditClick(Room room) {
        this.roomForEdit = room;
        showAddDialog(true);
    }

    @Override
    public void onRoomDeleteClick(Room room) {
        roomListViewModel.deleteRoom(room.getId(), room);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}





