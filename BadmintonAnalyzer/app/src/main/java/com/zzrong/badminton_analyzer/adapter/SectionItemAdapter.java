package com.zzrong.badminton_analyzer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.ExoPlayer;
import com.zzrong.badminton_analyzer.activity.SubExoPlayer;
import com.zzrong.badminton_analyzer.fragment.SubSectionFragment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.ViewHolder> {

    private ArrayList<List<Object>> itemList;
    private Fragment frag;
    private Context context;
    private Boolean[] bool;
    int start;

    public SectionItemAdapter(Fragment frag,Context context, ArrayList<List<Object>> lst, int startPos){
        this.frag = frag;
        this.context = context;
        itemList = lst;
        start = startPos;
        bool = ((ExoPlayer)context).getViewModel().getRecyclerState().getValue();
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

        holder.tv.setText((String)item.get(0));
        if((Boolean) item.get(1) == true){
            holder.left.setGuidelinePercent(0.03f);
            holder.right.setGuidelinePercent(0.13f);
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
                String sect = (String) item.get(0);

                //update boolean[]
                ((ExoPlayer)context).getViewModel().updateRecyclerState(posOfAll);

                //get video info from bundle
                intent.putExtra("id",id);
                intent.putExtra("sect",sect);
                intent.putExtra("score",position);

                //get info of the section
                try {

                    intent.putExtra("game",getGame(posOfAll));
                    intent.putExtra("frame info", parseEachFrameInfo(posOfAll));
                    intent.putExtra("shot list", parseShotList(posOfAll));
                    intent.putExtra("type dict", parseStat(posOfAll));
                    intent.putExtra("move dict", parseMove(posOfAll));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                current.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    /**
     *
     * @param pos
     * @throws JSONException
     * @return HashMap<String,Object> : keys = {type, start, end}
     */

    public int getGame(int pos) throws JSONException {
        String parsableStr = ((SubSectionFragment)frag).getData();
        JSONArray jsonArray = new JSONArray(parsableStr);
        int game = jsonArray.getJSONObject(pos).getInt("game");
        return game;
    }

    public HashMap<String, ArrayList<HashMap<String,Object>>> parseEachFrameInfo(int pos) throws JSONException {
        ArrayList<HashMap<String,Object>> frameInfoArr = new ArrayList<>();

        String parsableStr = ((SubSectionFragment)frag).getData();
        JSONArray jsonArr = new JSONArray(parsableStr);
        JSONArray shotJSONArr = jsonArr.getJSONObject(pos).getJSONArray("shot list");
        JSONArray moveJSONArr = jsonArr.getJSONObject(pos).getJSONArray("move direction list");
        boolean isBlueServe = jsonArr.getJSONObject(pos).getBoolean("blue serve first");

        for(int i = 0; i < shotJSONArr.length(); i++) {

            HashMap<String, Object> map = new HashMap<>();

            String moveType = moveJSONArr.getJSONArray(i).getString(0);
            String shotType = shotJSONArr.getJSONArray(i).getString(0).split(" ")[1];
            int start = shotJSONArr.getJSONArray(i).getInt(1);
            int end = shotJSONArr.getJSONArray(i).getInt(2);

            map.put("index", i);
            map.put("startFrame", start);
            map.put("midFrame", (start+end)/2);
            map.put("moveType", moveType);
            map.put("ballType", shotType);
            map.put("isBlueServe", isBlueServe); //和moved player相反

            isBlueServe = !isBlueServe;
            frameInfoArr.add(map);
        }

        HashMap<String, ArrayList<HashMap<String,Object>>> res = new HashMap();
        res.put("info", frameInfoArr);
        return res;
    }


    public HashMap<String, ArrayList<HashMap<String,Object>>> parseShotList(int pos) throws JSONException {
        ArrayList<HashMap<String,Object>> blueArray = new ArrayList<>();
        ArrayList<HashMap<String,Object>> redArray = new ArrayList<>();

        String parsableStr = ((SubSectionFragment)frag).getData();
        JSONArray jsonArray = new JSONArray(parsableStr);
        boolean isBlueServe = jsonArray.getJSONObject(pos).getBoolean("blue serve first");
        jsonArray = jsonArray.getJSONObject(pos).getJSONArray("shot list");

        for(int i = 0; i < jsonArray.length(); i++) {

            HashMap<String, Object> map = new HashMap<>();

            String type = jsonArray.getJSONArray(i).getString(0).split(" ")[1];
            int start = jsonArray.getJSONArray(i).getInt(1);
            int end = jsonArray.getJSONArray(i).getInt(2);

            map.put("type", type);
            map.put("start", start);
            map.put("end", end);
            map.put("serve", isBlueServe);

            if(isBlueServe){
                blueArray.add(map);
            }
            else{
                redArray.add(map);
            }

            isBlueServe = !isBlueServe; //next player's shot

        }

        HashMap<String, ArrayList<HashMap<String,Object>>> res = new HashMap();
        res.put("blue", blueArray);
        res.put("red", redArray);
        return res;

    }

    public HashMap<String, HashMap<String, Integer>> parseStat(int pos) throws JSONException {
        HashMap<String, Integer> blueDict = new HashMap<>();
        HashMap<String, Integer> redDict = new HashMap<>();
        String parsableStr = ((SubSectionFragment)frag).getData();
        JSONArray jsonArray = new JSONArray(parsableStr);

        JSONObject jsonObj = jsonArray.getJSONObject(pos).getJSONObject("blue shot dict");
        blueDict.put("長球", jsonObj.getInt("長球"));
        blueDict.put("切球", jsonObj.getInt("切球"));
        blueDict.put("殺球", jsonObj.getInt("殺球"));
        blueDict.put("挑球", jsonObj.getInt("挑球"));
        blueDict.put("小球", jsonObj.getInt("小球"));
        blueDict.put("平球", jsonObj.getInt("平球"));
        blueDict.put("撲球", jsonObj.getInt("撲球"));

        jsonObj = jsonArray.getJSONObject(pos).getJSONObject("red shot dict");
        redDict.put("長球", jsonObj.getInt("長球"));
        redDict.put("切球", jsonObj.getInt("切球"));
        redDict.put("殺球", jsonObj.getInt("殺球"));
        redDict.put("挑球", jsonObj.getInt("挑球"));
        redDict.put("小球", jsonObj.getInt("小球"));
        redDict.put("平球", jsonObj.getInt("平球"));
        redDict.put("撲球", jsonObj.getInt("撲球"));

        HashMap<String, HashMap<String,Integer>> map = new HashMap<>();
        map.put("blue", blueDict);
        map.put("red", redDict);

        return map;
    }

    public HashMap<String, HashMap<String, Float>> parseMove(int pos) throws JSONException {
        HashMap<String, Float> blueDict = new HashMap<>();
        HashMap<String, Float> redDict = new HashMap<>();
        String parsableStr = ((SubSectionFragment)frag).getData();
        JSONArray jsonArray = new JSONArray(parsableStr);

        JSONObject jsonObj = jsonArray.getJSONObject(pos).getJSONObject("blue move dict");
        blueDict.put("DLBL", (float)jsonObj.getDouble("DLBL"));
        blueDict.put("DLBR", (float)jsonObj.getDouble("DLBR"));
        blueDict.put("DLFL", (float)jsonObj.getDouble("DLFL"));
        blueDict.put("DLFR", (float)jsonObj.getDouble("DLFR"));
        blueDict.put("DSBL", (float)jsonObj.getDouble("DSBL"));
        blueDict.put("DSBR", (float)jsonObj.getDouble("DSBR"));
        blueDict.put("DSFL", (float)jsonObj.getDouble("DSFL"));
        blueDict.put("DSFR", (float)jsonObj.getDouble("DSFR"));
        blueDict.put("LLB", (float)jsonObj.getDouble("LLB"));
        blueDict.put("LLF", (float)jsonObj.getDouble("LLF"));
        blueDict.put("LSB", (float)jsonObj.getDouble("LSB"));
        blueDict.put("LSF", (float)jsonObj.getDouble("LSF"));
        blueDict.put("TLL", (float)jsonObj.getDouble("TLL"));
        blueDict.put("TLR", (float)jsonObj.getDouble("TLR"));
        blueDict.put("TSL", (float)jsonObj.getDouble("TSL"));
        blueDict.put("TSR", (float)jsonObj.getDouble("TSR"));
        blueDict.put("NM", (float)jsonObj.getDouble("NM"));

        jsonObj = jsonArray.getJSONObject(pos).getJSONObject("red move dict");
        redDict.put("DLBL", (float)jsonObj.getDouble("DLBL"));
        redDict.put("DLBR", (float)jsonObj.getDouble("DLBR"));
        redDict.put("DLFL", (float)jsonObj.getDouble("DLFL"));
        redDict.put("DLFR", (float)jsonObj.getDouble("DLFR"));
        redDict.put("DSBL", (float)jsonObj.getDouble("DSBL"));
        redDict.put("DSBR", (float)jsonObj.getDouble("DSBR"));
        redDict.put("DSFL", (float)jsonObj.getDouble("DSFL"));
        redDict.put("DSFR", (float)jsonObj.getDouble("DSFR"));
        redDict.put("LLB", (float)jsonObj.getDouble("LLB"));
        redDict.put("LLF", (float)jsonObj.getDouble("LLF"));
        redDict.put("LSB", (float)jsonObj.getDouble("LSB"));
        redDict.put("LSF", (float)jsonObj.getDouble("LSF"));
        redDict.put("TLL", (float)jsonObj.getDouble("TLL"));
        redDict.put("TLR", (float)jsonObj.getDouble("TLR"));
        redDict.put("TSL", (float)jsonObj.getDouble("TSL"));
        redDict.put("TSR", (float)jsonObj.getDouble("TSR"));
        redDict.put("NM", (float)jsonObj.getDouble("NM"));

        HashMap<String, HashMap<String,Float>> map = new HashMap<>();
        map.put("blue", blueDict);
        map.put("red", redDict);

        return map;
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
