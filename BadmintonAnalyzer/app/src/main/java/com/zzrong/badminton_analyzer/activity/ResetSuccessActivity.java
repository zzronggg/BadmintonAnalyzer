package com.zzrong.badminton_analyzer.activity;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.zzrong.badminton_analyzer.R;

public class ResetSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_success);
        initialize();
    }

    public void initialize(){
        TextView tv = findViewById(R.id.tv_reset_suc_go_back);
        tv.setOnClickListener(v -> backToHomePage());
    }

    public void backToHomePage(){
        setResult(RESULT_OK);
        finish();
    }
}