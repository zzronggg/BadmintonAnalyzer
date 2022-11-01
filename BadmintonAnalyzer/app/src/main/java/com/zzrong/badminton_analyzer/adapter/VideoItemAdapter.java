package com.zzrong.badminton_analyzer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.YouTubeActivity;
import com.zzrong.badminton_analyzer.func.VideoItem;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class VideoItemAdapter  extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {

    private ArrayList<VideoItem> itemList;
    private Context context;

    public VideoItemAdapter(Context context, ArrayList<VideoItem> lst){
        this.context = context;
        itemList = lst;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final VideoItem item = itemList.get(position);
        Picasso.get().load(item.getPic()).into(holder.imageId);
        holder.textId.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context current = v.getContext();
                Intent intent = new Intent(current, YouTubeActivity.class);
                intent.putExtra("id",item.getID());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("thumb", item.getPic());
////                *************************
//                intent.putExtra("time",0);
                current.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageId;
        TextView textId;
        ViewHolder(View itemView) {
            super(itemView);
            imageId = (ImageView) itemView.findViewById(R.id.imageId);
            textId = (TextView) itemView.findViewById(R.id.textId);
        }
    }


}
