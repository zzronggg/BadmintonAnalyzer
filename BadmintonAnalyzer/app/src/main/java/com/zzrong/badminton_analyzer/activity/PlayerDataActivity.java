package com.zzrong.badminton_analyzer.activity;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.fragment.*;
import com.zzrong.badminton_analyzer.func.FlaskApiSender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class PlayerDataActivity extends AppCompatActivity {
    private TabLayout mainTab;
    private TabLayout subTab;
    private Fragment currentFrag;
    private ArrayList<Fragment> blueFrags;
    private ArrayList<Fragment> redFrags;
    private HashMap<String,HashMap<String,Float>> totalInfo;
    private String vid;
    private boolean isBluePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_data);
        vid = getIntent().getStringExtra("vid");
        isBluePlayer = getIntent().getBooleanExtra("is_blue", true);
    }

    protected void onStart() {
        super.onStart();
        setBar();
        setTab();
        try {
            getData();
        } catch (JSONException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            initFrag();
        } catch (InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onResume(){

        super.onResume();
        if(!isBluePlayer){
            mainTab.getTabAt(0).select();
            subTab.getTabAt(0).select();
            mainTab.getTabAt(1).select();
        }
    }

    private void setBar(){
        MaterialToolbar bar = findViewById(R.id.bar_player_data);
        bar.setNavigationOnClickListener(v -> finish());
    }

    private void setTab() {

        mainTab = findViewById(R.id.tab_player_data);
        subTab = findViewById(R.id.tab_player_data_sub);

        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case(0):{
                        mainTab.setTabTextColors(Color.parseColor("#535353"), Color.parseColor("#43B5F5"));
                        mainTab.setSelectedTabIndicatorColor(Color.parseColor("#43B5F5"));
                        switchFrag();
                        return;
                    }
                    case(1):{
                        mainTab.setTabTextColors(Color.parseColor("#535353"), Color.parseColor("#FD736E"));
                        mainTab.setSelectedTabIndicatorColor(Color.parseColor("#FD736E"));
                        switchFrag();
                        return;
                    }
                    default:{}
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        subTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFrag();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getData() throws JSONException, InterruptedException {

        totalInfo = new HashMap<>();
        final String[] response = new String[1];
        Thread t = new Thread(() -> response[0] = FlaskApiSender.getTotalInfo(vid));

        t.start();
        t.join();

        JSONObject json = new JSONObject(response[0]);

        JSONObject bts = json.getJSONObject("blue total shots");
        JSONObject bws = json.getJSONObject("blue win shots");
        JSONObject rts = json.getJSONObject("red total shots");
        JSONObject rws = json.getJSONObject("red win shots");
        JSONObject btm = json.getJSONObject("blue total move");
        JSONObject rtm = json.getJSONObject("red total move");

        //parse information
        totalInfo.put("blue_total_shots", extractShots(bts));
        totalInfo.put("blue_win_shots", extractShots(bws));
        totalInfo.put("red_total_shots", extractShots(rts));
        totalInfo.put("red_win_shots", extractShots(rws));
        totalInfo.put("blue_total_move", extractMoves(btm));
        totalInfo.put("red_total_move", extractMoves(rtm));


    }

    private void initFrag() throws InterruptedException, JSONException {

        JSONObject jsonObj = new JSONObject(FlaskApiSender.getHighlightInfo(vid));

        String[] blueWinKey, blueLossKey;

        JSONArray key;

        if(!jsonObj.isNull("blue win key")) {
            key = jsonObj.getJSONArray("blue win key");
            blueWinKey = new String[]{ key.getString(0),
                    key.getString(1),
                    key.getString(2)};
        }
        else{
            blueWinKey = null;
        }

        if(!jsonObj.isNull("blue loss key")) {
            key = jsonObj.getJSONArray("blue loss key");
            blueLossKey = new String[]{ key.getString(0),
                    key.getString(1),
                    key.getString(2)};
        }else{
            blueLossKey = null;
        }

        String[] redWinKey, redLossKey;

        if(!jsonObj.isNull("red win key")) {
            key = jsonObj.getJSONArray("red win key");
            redWinKey = new String[]{ key.getString(0),
                    key.getString(1),
                    key.getString(2)};
        }
        else{
            redWinKey = null;
        }

        if(!jsonObj.isNull("red loss key")) {
            key = jsonObj.getJSONArray("red loss key");
            redLossKey = new String[]{ key.getString(0),
                    key.getString(1),
                    key.getString(2)};
        }
        else{
            redLossKey = null;
        }


        boolean hasBlueHighlight, hasRedHighlight;
        hasBlueHighlight = jsonObj.getBoolean("blue highlights");
        hasRedHighlight = jsonObj.getBoolean("red highlights");

        Fragment b1 = WinRateFragment.newInstance(totalInfo.get("blue_total_shots"), totalInfo.get("blue_win_shots"));
        Fragment b2 = StrategyFragment.newInstance(vid, hasBlueHighlight, blueWinKey, blueLossKey, true);
        Fragment b3 = MoveRateFragment.newInstance(totalInfo.get("blue_total_move"));

        Fragment r1 = WinRateFragment.newInstance(totalInfo.get("red_total_shots"), totalInfo.get("red_win_shots"), false);
        Fragment r2 = StrategyFragment.newInstance(vid, hasRedHighlight, redWinKey, redLossKey, false);
        Fragment r3 = MoveRateFragment.newInstance(totalInfo.get("red_total_move"), false);

        blueFrags = new ArrayList<>(Arrays.asList(b1, b2, b3));
        redFrags = new ArrayList<>(Arrays.asList(r1, r2, r3));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frag_view_player_data, blueFrags.get(0), "f_b_ball");
        ft.add(R.id.frag_view_player_data, blueFrags.get(1), "f_b_strategy");
        ft.add(R.id.frag_view_player_data, blueFrags.get(2), "f_b_move");
        ft.add(R.id.frag_view_player_data, redFrags.get(0), "f_r_ball");
        ft.add(R.id.frag_view_player_data, redFrags.get(1), "f_r_strategy");
        ft.add(R.id.frag_view_player_data, redFrags.get(2), "f_r_move");

        ft.hide(blueFrags.get(1));
        ft.hide(blueFrags.get(2));
        ft.hide(redFrags.get(0));
        ft.hide(redFrags.get(1));
        ft.hide(redFrags.get(2));

        ft.commit();

        currentFrag = blueFrags.get(0);

    }

    //call this function when main tab or sub tab change
    public void switchFrag(){

        Fragment newFrag;
        int[] pos = new int[] {mainTab.getSelectedTabPosition(), subTab.getSelectedTabPosition()};

        if(pos[0] == 0){ //blue player
           newFrag = blueFrags.get(pos[1]);
        }
        else{  //red player
           newFrag = redFrags.get(pos[1]);
        }

        if(currentFrag != newFrag){

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(currentFrag);
            ft.show(newFrag);
            ft.commit();

            currentFrag = newFrag;

        }

    }

    public HashMap<String, Float> extractShots(JSONObject obj) throws JSONException {
        HashMap<String, Float> map = new HashMap<>();
        map.put("長球", (float) (obj.getDouble("長球")));
        map.put("切球", (float) (obj.getDouble("切球")));
        map.put("殺球", (float) (obj.getDouble("殺球")));
        map.put("挑球", (float) (obj.getDouble("挑球")));
        map.put("小球", (float) (obj.getDouble("小球")));
        map.put("平球", (float) (obj.getDouble("平球")));
        map.put("撲球", (float) (obj.getDouble("撲球")));
//        map.put("拉吊", (float) (obj.getDouble("拉吊")));
//        map.put("搶攻", (float) (obj.getDouble("搶攻")));
        return map;
    }
    public HashMap<String, Float> extractMoves(JSONObject obj) throws JSONException {
        HashMap<String, Float> map = new HashMap<>();
        map.put("DLBL", (float) (obj.getDouble("DLBL")));
        map.put("DLBR", (float) (obj.getDouble("DLBR")));
        map.put("DLFL", (float) (obj.getDouble("DLFL")));
        map.put("DLFR", (float) (obj.getDouble("DLFR")));
        map.put("DSBL", (float) (obj.getDouble("DSBL")));
        map.put("DSBR", (float) (obj.getDouble("DSBR")));
        map.put("DSFL", (float) (obj.getDouble("DSFL")));
        map.put("DSFR", (float) (obj.getDouble("DSFR")));
        map.put("LLB", (float) (obj.getDouble("LLB")));
        map.put("LLF", (float) (obj.getDouble("LLF")));
        map.put("LSB", (float) (obj.getDouble("LSB")));
        map.put("LSF", (float) (obj.getDouble("LSF")));
        map.put("TLL", (float) (obj.getDouble("TLL")));
        map.put("TLR", (float) (obj.getDouble("TLR")));
        map.put("TSL", (float) (obj.getDouble("TSL")));
        map.put("TSR", (float) (obj.getDouble("TSR")));
        map.put("NM", (float) (obj.getDouble("NM")));
        return map;
    }

    public int getTabPos(){
        TabLayout tabLayout = findViewById(R.id.tab_player_data);
        return tabLayout.getSelectedTabPosition();
    }
}