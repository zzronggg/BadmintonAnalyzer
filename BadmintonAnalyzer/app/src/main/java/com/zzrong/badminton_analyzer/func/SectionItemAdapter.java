package com.zzrong.badminton_analyzer.func;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.act.ExoPlayer;
import com.zzrong.badminton_analyzer.act.SubExoPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;
    private Boolean[] bool;

    public SectionItemAdapter(Context context, ArrayList<String> lst){
        this.context = context;
        itemList = lst;
    }
    public SectionItemAdapter(Context context, ArrayList<String> lst, Boolean[] b){
        this(context,lst);
        bool = b;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final String item = itemList.get(position);
        holder.tv.setText(item);
        holder.itemView.setAlpha(0.3f);

        //restored recycler
        if(bool!=null){
            if(bool[position] == true) {
                ((CardView) holder.itemView).setCardElevation(10);
                holder.itemView.setAlpha(1);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CardView)v).setCardElevation(0);
                v.setAlpha(1);
                Context current = v.getContext();
                Intent intent = new Intent(current, SubExoPlayer.class);
                String id  = ((ExoPlayer)current).getVid();
                String sect = item.replace(":","");

                ((ExoPlayer)context).getViewModel().updateRecyclerState(position);

                //get video info from bundle
                intent.putExtra("id",id);
                intent.putExtra("sect",sect);

                //get the remaining info through api
                intent.putExtra("pred", FlaskApiSender.getSectPred(id,sect));
                intent.putExtra("stat", FlaskApiSender.getSectStat(id,sect));
                current.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        int n;
        ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.sectText);
            n=0;
        }
    }


}
