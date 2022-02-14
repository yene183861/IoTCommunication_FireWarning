package vn.hust.soict.project.iotcommunication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.model.Device;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.deviceViewHolder> {
    private Context context;
    private List<Device> deviceList;
    private DeviceListAdapter.ItemClickListener clickListener;

    public DeviceListAdapter(Context context, List<Device> deviceList, ItemClickListener clickListener ){
        this.context = context;
        this.deviceList = deviceList;
        this.clickListener = clickListener;
    }
    public void setDeviceList(List<Device> deviceList){
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public deviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceListAdapter.deviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull deviceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDeviceName.setText(this.deviceList.get(position).getName());
        holder.tvDevicePosition.setText(this.deviceList.get(position).getPosition());
        int type = this.deviceList.get(position).getType();
            if (type == 1){ //lua
                holder.imgDevice.setImageResource(R.drawable.fire_sensor);
            } else if(type == 2){ //gas
                holder.imgDevice.setImageResource(R.drawable.smoke_sensor);
            }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListener.onSensorClick(sensorList.get(position));
//            }
//        });

        holder.imgEditDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDeviceEditClick(deviceList.get(position));
            }
        });
        holder.imgDeleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDeviceDeleteClick(deviceList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.deviceList != null) {
            return this.deviceList.size();
        }
        return 0;
    }

    public class deviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDevice, imgEditDevice, imgDeleteDevice;
        private TextView tvValue, tvDeviceName, tvDevicePosition;
        public deviceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDevice= itemView.findViewById(R.id.imgDevice);
            imgEditDevice = itemView.findViewById(R.id.imgEditDevice);
            imgDeleteDevice = itemView.findViewById(R.id.imgDeleteDevice);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvDevicePosition = itemView.findViewById(R.id.tvDevicePosition);
        }
    }
    public interface ItemClickListener{
//        void onDeviceClick(Device sensor);
        void onDeviceEditClick(Device device);
        void onDeviceDeleteClick(Device device);
    }
}

