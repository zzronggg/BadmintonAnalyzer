package com.zzrong.badminton_analyzer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.ExoPlayer;
import com.zzrong.badminton_analyzer.activity.SubExoPlayer;
import com.zzrong.badminton_analyzer.func.FlaskApiSender;
import com.zzrong.badminton_analyzer.func.SampleProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.ViewHolder> {

    private ArrayList<List<Object>> itemList;
    private Context context;
    private Boolean[] bool;
    int start;

    public SectionItemAdapter(Context context, ArrayList<List<Object>> lst, int startPos){
        this.context = context;
        itemList = lst;
        start = startPos;
    }
    public SectionItemAdapter(Context context, ArrayList<List<Object>> lst, int startPos,Boolean[] b){
        this(context,lst,startPos);
        bool = b;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final List<Object> item = itemList.get(position);
        final int posOfAll = start + position;
        Log.d("posInfo", posOfAll +"");

        holder.tv.setText((String)item.get(0));
        if((Boolean) item.get(1) == true){
            holder.btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.elegant_blue)));
        }
        else{
            holder.left.setGuidelinePercent(0.87f);
            holder.right.setGuidelinePercent(0.97f);
            holder.btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.elegant_red)));
        }


        //Initial opacity of recycler items
        ((CardView) holder.itemView).setCardElevation(10);
        holder.itemView.setAlpha(1);

        //restored recycler clicked state
        if(bool!=null){
            Log.d("posTest: ", bool.length + "");
            Log.d("posTest: ", position + "");
            if(bool[posOfAll] == true) {
                ((CardView) holder.itemView).setCardElevation(0);
                holder.itemView.setAlpha(0.3f);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //start SubExoPlayer
                ((CardView)v).setCardElevation(0);
                v.setAlpha(0.3f);
                Context current = v.getContext();
                Intent intent = new Intent(current, SubExoPlayer.class);
                String id  = ((ExoPlayer)current).getVid();
                String sect = ((String) item.get(0)).replace(":","");


                //update boolean[]
                ((ExoPlayer)context).getViewModel().updateRecyclerState(posOfAll);

                //get video info from bundle
                intent.putExtra("id",id);
                intent.putExtra("sect",sect);

                //get the remaining info through api
//                intent.putExtra("pred_up", FlaskApiSender.getSectPred_up(id,sect));
//                intent.putExtra("pred_down", FlaskApiSender.getSectPred_down(id,sect));
                intent.putExtra("pred_up", SampleProvider.predFragSampleUp());
                intent.putExtra("pred_down", SampleProvider.predFragSampleDown());

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
        Button btn;
        Guideline left;
        Guideline right;
        ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.sectText);
            btn = itemView.findViewById(R.id.circle);
            left = itemView.findViewById(R.id.btn_left);
            right = itemView.findViewById(R.id.btn_right);
        }
    }


}
