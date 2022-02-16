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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.model.Device;
import vn.hust.soict.project.iotcommunication.model.Notification;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.notificationViewHolder> {
    private Context context;
    private List<Notification> list;

    public NotificationListAdapter(Context context, List<Notification> list){
        this.context = context;
        this.list = list;
    }
    public void setNotificationList(List<Notification> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new NotificationListAdapter.notificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDeviceName.setText(this.list.get(position).getDevice());
        holder.tvContent.setText(this.list.get(position).getContent());
        holder.tvTime.setText(convertDateTime(this.list.get(position).getMilliseconds()));
        holder.img.setImageResource(R.drawable.ic_ring);
    }

    @Override
    public int getItemCount() {
        if(this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public class notificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, imgEditRoom, imgDeleteRoom;
        private TextView tvDeviceName, tvContent, tvTime;
        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgRoom);
            imgEditRoom = itemView.findViewById(R.id.imgEditRoom);
            imgDeleteRoom = itemView.findViewById(R.id.imgDeleteRoom);
            tvDeviceName = itemView.findViewById(R.id.tvRoomName);
            tvTime = itemView.findViewById(R.id.tvRoomPosition);
            tvContent = itemView.findViewById(R.id.tvRoomOwner);
            imgEditRoom.setVisibility(View.GONE);
            imgDeleteRoom.setVisibility(View.GONE);
            tvDeviceName.setVisibility(View.GONE);
        }
    }
    private String convertDateTime(long milliseconds){
        Date date = new Date(milliseconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}

