package com.zzrong.badminton_analyzer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.zzrong.badminton_analyzer.BuildConfig;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.viewModel.StrategyViewModel;

public class StrategyFragment extends Fragment {

    private boolean isTop, hasHighlight;
    private String[] winSeq, loseSeq;
    private String vid;
    private StrategyViewModel model;


    //video player
    String url;
    long currentTime;
    private com.google.android.exoplayer2.ExoPlayer player;
    private StyledPlayerView playerView;

    public StrategyFragment() {
        // Required empty public constructor
    }

    public static StrategyFragment newInstance(String vid, Boolean highlight, String[] win, String[] lose, boolean isTopPlayer) {
        StrategyFragment fragment = new StrategyFragment();
        Bundle args = new Bundle();
        args.putString("vid", vid);
        args.putStringArray("win", win);
        args.putStringArray("lose", lose);
        args.putBoolean("top", isTopPlayer);
        args.putBoolean("highlight", highlight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vid = getArguments().getString("vid");
            winSeq = getArguments().getStringArray("win");
            loseSeq = getArguments().getStringArray("lose");
            isTop = getArguments().getBoolean("top");
            hasHighlight = getArguments().getBoolean("highlight");
        }

        model = new ViewModelProvider(getActivity()).get(StrategyViewModel.class);
        model.getPlayTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player!=null) {
            releasePlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_strategy, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        setHighlight();
        setVideo();
        if(hasHighlight) {
            resumePlayer();
        }
    }

    public void setHighlight(){
        TextView tvWin = getView().findViewById(R.id.tv_win_seq);
        TextView tvLose = getView().findViewById(R.id.tv_lose_seq);

//        winSeq = new String[]{"長球","挑球","長球"};
//        loseSeq = new String[]{"長球","殺球","長球"};

        if(winSeq != null) {
            String str = winSeq[0] + " → " + winSeq[1] + " → " + winSeq[2];
            tvWin.setText(str);
        }
        else{
            tvWin.setText("無");
        }
        if(loseSeq != null) {
            String str = loseSeq[0] + " → " + loseSeq[1] + " → " + loseSeq[2];
            tvLose.setText(str);
        }
        else{
            tvLose.setText("無");
        }
    }

    public void setVideo(){
        setURL();
        initPlayer();
    }


    private void setURL(){
        String base = BuildConfig.SVR_IP;      //res location in server
        String filename;

        if(hasHighlight) {

            if (isTop) {
                filename = "blue_highlights.mp4";
            } else {
                filename = "red_highlights.mp4";
            }

            url = base + vid + "/" + filename;
        }
        else{
            url = base + "no_highlight.mp4";
        }
        Log.d("URL: ",url);
    }

    private void resumePlayer(){
        try {
            if(model.getPlayTime()!=null) {
                currentTime = model.getPlayTime().getValue();
                Log.d("Current: ", currentTime + "");
                player.seekTo(currentTime);
            }
        }
        catch (Exception e){
            Log.e("NullPointerException: ", "first time created");
        }
        player.setPlayWhenReady(true);
    }

    private void initPlayer(){
        setURL();
        player = new com.google.android.exoplayer2.ExoPlayer.Builder(getContext()).build();
        playerView = getView().findViewById(R.id.strat_exo_viewer);
        playerView.setFullscreenButtonClickListener(
                isFullScreen -> {

//                    int orientation = getResources().getConfiguration().orientation;
//                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    }
//                    else{
//                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }

                }
        );
        playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(getContext());
        Uri uri = Uri.parse(url);
        MediaSource src = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
        player.setMediaSource(src);
        player.prepare();
    }

    private void releasePlayer(){
        currentTime = player.getCurrentPosition();
        model.setPlayTime(currentTime);
        player.setPlayWhenReady(false);
        player.stop();
        playerView.onPause();
        player.release();
    }
}