package vn.hust.soict.project.iotcommunication.ui;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.common.util.NumberUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import vn.hust.soict.project.iotcommunication.adapter.HomeListAdapter;
import vn.hust.soict.project.iotcommunication.viewmodel.HomeListViewModel;
import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.model.Home;

public class ManageHomeFragment extends Fragment implements HomeListAdapter.ItemClickListener{
    private ImageView btnAddNew;
    private TextView tvNoHomeList;
    private RecyclerView recyclerView;

    private HomeListViewModel homeListViewModel;
    private Context mContext;

    private List<Home> homeList;
    private HomeListAdapter adapter;
    private Home homeForEdit;
    ImageView imageHome;
    Uri img = null;

    public ManageHomeFragment() {
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
        View view = inflater.inflate(R.layout.layout_fragment_list_home, container, false);

        btnAddNew = view.findViewById(R.id.btnAddNew);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvNoHomeList = view.findViewById(R.id.tvNoHomeList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HomeListAdapter(mContext,homeList, this);
        recyclerView.setAdapter(adapter);

        homeListViewModel = new ViewModelProvider(this).get(HomeListViewModel.class);

        homeListViewModel.getHomesListObserver().observe(getViewLifecycleOwner(), new Observer<List<Home>>() {
            @Override
            public void onChanged(List<Home> homes) {
                if(homes == null){
                    recyclerView.setVisibility(View.GONE);
                    tvNoHomeList.setVisibility(View.VISIBLE);
                } else {
                    homeList = homes;
                    adapter.setHomeList(homes);
                    tvNoHomeList.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });
        homeListViewModel.getHomeList();

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
        return view;
    }

    private void showAddDialog(boolean isEdit){
        AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_home, null);
        TextView addHomeTitleDialog = dialogView.findViewById(R.id.addHomeTitleDialog);
        //EditText enterUser = dialogView.findViewById(R.id.enterUser);
        EditText enterHomeName = dialogView.findViewById(R.id.enterHomeName);
        EditText enterHomeAddress = dialogView.findViewById(R.id.enterHomeAddress);
        EditText enterHomeArea = dialogView.findViewById(R.id.enterHomeArea);
        EditText enterRooms = dialogView.findViewById(R.id.enterRooms);
        EditText enterFloor = dialogView.findViewById(R.id.enterFloor);
        EditText enterMembers = dialogView.findViewById(R.id.enterMembers);
        TextView btnCreate = dialogView.findViewById(R.id.btnCreate);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        ImageView chooseHomeImage = dialogView.findViewById(R.id.chooseHomeImage);
        imageHome = dialogView.findViewById(R.id.imageHome);
        LinearLayout layout = dialogView.findViewById(R.id.layoutImgHome);
        //layout.setVisibility(View.GONE);

        if(isEdit){
            addHomeTitleDialog.setText("Update information home");
            btnCreate.setText("Update");
            //enterUser.setText(homeForEdit.getUser());
            enterHomeName.setText(homeForEdit.getName());
            enterHomeAddress.setText(homeForEdit.getAddress());
            enterHomeArea.setText(String.valueOf(homeForEdit.getArea()));
            enterRooms.setText(String.valueOf(homeForEdit.getRooms()));
            enterFloor.setText(String.valueOf(homeForEdit.getFloor()));
            enterMembers.setText(String.valueOf(homeForEdit.getMembers()));
//            layout.setVisibility(View.GONE);
//                try {
//                    imageHome.setImageBitmap(MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), homeForEdit.getImage()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        }
        chooseHomeImage.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            requestPermission();
        }
    });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String user = enterUser.getText().toString().trim();
                String name = enterHomeName.getText().toString().trim();
                String addr = enterHomeAddress.getText().toString().trim();
                int area = Integer.parseInt(enterHomeArea.getText().toString().trim());
                int rooms = Integer.parseInt(enterRooms.getText().toString().trim());
                int floor = Integer.parseInt(enterFloor.getText().toString().trim());
                int members = Integer.parseInt(enterMembers.getText().toString().trim());

                //check fields empty
//                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(addr) || )
                //call view model
                if(isEdit){
                   homeForEdit.setName(name);
                    homeForEdit.setAddress(addr);
                    homeForEdit.setArea(area);
                    homeForEdit.setFloor(floor);
                    homeForEdit.setMembers(members);
                    homeForEdit.setRooms(rooms);


//                    homeForEdit.setImage(img);
                   homeListViewModel.updateHome(homeForEdit.getId(), homeForEdit);
                //    Log.e("id", homeForEdit.getId());
                } else {
                    //call view model
                    Home home = new Home(name, addr, area, rooms, floor, members, null);
                    homeListViewModel.insertHome(home);
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
                            imageHome.setImageBitmap(bitmap);
                            imageHome.setVisibility(View.VISIBLE);
                            img = getImageUri(mContext, bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onHomeClick(Home home) {
        FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ManageRoomFragment fragment = new ManageRoomFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("home", home);
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onHomeEditClick(Home home) {
        this.homeForEdit = home;
        showAddDialog(true);
    }

    @Override
    public void onHomeDeleteClick(Home home) {
        homeListViewModel.deleteHome(home.getId(),home);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
