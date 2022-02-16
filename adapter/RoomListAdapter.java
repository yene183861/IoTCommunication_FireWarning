package vn.hust.soict.project.iotcommunication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.Room;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.roomViewHolder> {
    private Context context;
    private List<Room> roomList;
    private RoomListAdapter.ItemClickListener clickListener;

    public RoomListAdapter(Context context,List<Room> roomList, ItemClickListener clickListener ){
        this.context = context;
        this.roomList = roomList;
        this.clickListener = clickListener;
    }
    public void setRoomList(List<Room> roomList){
        this.roomList = roomList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public roomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomListAdapter.roomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull roomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String owner = this.roomList.get(position).getOwner();
        holder.tvRoomPosition.setText(this.roomList.get(position).getPosition());
        holder.tvRoomOwner.setText("Owner: " + owner);
            int type = this.roomList.get(position).getType();
            switch (type){
                case 1 : holder.imgRoom.setImageResource(R.drawable.living_room);
                    holder.tvRoomName.setText("Living room");
                    break;
                case 2 : holder.imgRoom.setImageResource(R.drawable.bedroom2);
                    holder.tvRoomName.setText(owner + "'s bed room");
                    break;
                case 3 : holder.imgRoom.setImageResource(R.drawable.kitchen);
                    holder.tvRoomName.setText("Kitchen");break;
                case 4 : holder.imgRoom.setImageResource(R.drawable.dinningroom);
                    holder.tvRoomName.setText("Dining room");break;
                case 5 : holder.imgRoom.setImageResource(R.drawable.bathroom);
                    holder.tvRoomName.setText("Bathroom");break;
            }
//        } else {
//            Glide.with(context)
//                    .load(this.roomList.get(position).getThumbnail())
//                    .apply(RequestOptions.centerCropTransform())
//                    .into(holder.imgRoom);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRoomClick(roomList.get(position));
            }
        });

        holder.imgEditRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRoomEditClick(roomList.get(position));
            }
        });
        holder.imgDeleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRoomDeleteClick(roomList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.roomList != null) {
            return this.roomList.size();
        }
        return 0;
    }

    public class roomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRoom, imgEditRoom, imgDeleteRoom;
        private TextView tvRoomName, tvRoomPosition, tvRoomOwner;
        public roomViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            imgEditRoom = itemView.findViewById(R.id.imgEditRoom);
            imgDeleteRoom = itemView.findViewById(R.id.imgDeleteRoom);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPosition = itemView.findViewById(R.id.tvRoomPosition);
            tvRoomOwner = itemView.findViewById(R.id.tvRoomOwner);
        }
    }
    public interface ItemClickListener{
        void onRoomClick(Room room);
        void onRoomEditClick(Room room);
        void onRoomDeleteClick(Room room);
    }
}

