package com.zzrong.badminton_analyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.zzrong.badminton_analyzer.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BallTypeItemAdapter extends RecyclerView.Adapter<BallTypeItemAdapter.ViewHolder> {

    private ArrayList<String> itemList;
//    private
    private Context context;

    public BallTypeItemAdapter(Context context, ArrayList<String> lst, boolean serve){
        this.context = context;
        if(!serve){  //test
            lst.add(0,"");
        }
        itemList = lst;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ball_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String item = itemList.get(position);
        //仍未考慮得分球路長度小於3之情形（因最後3球須用橘色標出得分球路）
        if(position >= itemList.size() - 3){
            holder.textId.setTextColor(ContextCompat.getColor(this.context, R.color.elegant_orange));
        }
        else {
            holder.textId.setTextColor(ContextCompat.getColor(this.context, R.color.black));
        }

        if(!item.isEmpty()){
            item = item.charAt(0) + "\n" + item.charAt(1);}
        holder.textId.setText(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textId;
        ViewHolder(View itemView) {
            super(itemView);
            textId = (TextView) itemView.findViewById(R.id.typeText);
        }
    }


}
