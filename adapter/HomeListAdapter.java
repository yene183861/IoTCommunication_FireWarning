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
import vn.hust.soict.project.iotcommunication.model.Home;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.homeViewHolder> {
private Context context;
private List<Home> homeList;
private ItemClickListener clickListener;

public HomeListAdapter(Context context,List<Home> homeList, ItemClickListener clickListener ){
    this.context = context;
    this.homeList = homeList;
    this.clickListener = clickListener;
}
public void setHomeList(List<Home> homeList){
    this.homeList = homeList;
    notifyDataSetChanged();
}

    @NonNull
    @Override
    public homeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeListAdapter.homeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull homeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvHomeName.setText(this.homeList.get(position).getName());
        holder.tvHomeAddress.setText(this.homeList.get(position).getAddress());
//        Glide.with(context)
//                .load(this.homeList.get(position).getImages())
//                .apply(RequestOptions.centerCropTransform())
//                .into(holder.imgHome);
//        if(this.homeList.get(position).getThumbnail() == null){
            holder.imgHome.setImageResource(R.drawable.house);
//        } else {
//            Glide.with(context)
//                    .load(this.homeList.get(position).getThumbnail())
//                    .apply(RequestOptions.centerCropTransform())
//                    .into(holder.imgHome);
        //}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onHomeClick(homeList.get(position));
            }
        });

        holder.imgEditHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onHomeEditClick(homeList.get(position));
            }
        });
        holder.imgDeleteHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onHomeDeleteClick(homeList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.homeList != null) {
            return this.homeList.size();
        }
        return 0;
    }

    public class homeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHome, imgEditHome, imgDeleteHome;
        private TextView tvHomeName, tvHomeAddress;
        public homeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHome = itemView.findViewById(R.id.imgHome);
            imgEditHome = itemView.findViewById(R.id.imgEditHome);
            imgDeleteHome = itemView.findViewById(R.id.imgDeleteHome);
            tvHomeName = itemView.findViewById(R.id.tvHomeName);
            tvHomeAddress = itemView.findViewById(R.id.tvHomeAddress);
        }
    }
    public interface ItemClickListener{
         void onHomeClick(Home home);
         void onHomeEditClick(Home home);
         void onHomeDeleteClick(Home home);
    }
}
