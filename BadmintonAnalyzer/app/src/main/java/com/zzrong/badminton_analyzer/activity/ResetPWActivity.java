package com.zzrong.badminton_analyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.zzrong.badminton_analyzer.R;

public class ResetPWActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);
        initialize();
        setListener();
    }

    public void initialize(){
    }

    public void setListener(){
        TextView tv = findViewById(R.id.tv_reset_go_back);
        tv.setOnClickListener(v -> finish());
        Button btn = findViewById(R.id.btn_reset_submit);

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            finish();
                        }
                    }
                });

        btn.setOnClickListener(v -> submitReset(resultLauncher));
    }

    public void submitReset(ActivityResultLauncher launcher){
//
//
//
        Intent intent = new Intent(this, ResetSuccessActivity.class);
        launcher.launch(intent);
    }
}