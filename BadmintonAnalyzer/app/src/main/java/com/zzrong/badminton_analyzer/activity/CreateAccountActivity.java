package com.zzrong.badminton_analyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.FlaskApiSender;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initialize();
        setListener();
    }

    protected void onResume(){
        super.onResume();

    }

    public void initialize() {
    }

    public void setListener() {
        TextView tv = findViewById(R.id.tv_create_go_back);
        tv.setOnClickListener(v -> finish());

        Button btn = findViewById(R.id.btn_create_submit);

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

        btn.setOnClickListener(v -> submitReg(resultLauncher));
    }

    public void submitReg(ActivityResultLauncher launcher) {

        String acc = ((TextInputLayout)findViewById(R.id.tf_create_username)).getEditText().getText().toString();
        String mail = ((TextInputLayout)findViewById(R.id.tf_create_email)).getEditText().getText().toString();
        String password = ((TextInputLayout)findViewById(R.id.tf_create_password)).getEditText().getText().toString();
        String password2 = ((TextInputLayout)findViewById(R.id.tf_create_confirm_password)).getEditText().getText().toString();

        //ensure no empty input and password confirmation
        Boolean anyEmpty = (acc.isEmpty() || mail.isEmpty() || password.isEmpty() || password2.isEmpty());
        Boolean isPwSame = (password.equals(password2));

        Log.d("Sign_up_success",anyEmpty.toString());
        Log.d("Sign_up_success",isPwSame.toString());
        if(anyEmpty){
//
//          Warning Toast: input cannot be empty
//
            return;
        }

        if(!isPwSame){
//
//          Warning Toast: two passwords are not same
//
            return;
        }
        Log.d("Sign_up_success","Send request");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Boolean success = FlaskApiSender.signUp(acc,mail,password);
                Log.d("Sign_up_success",success.toString());
                if(success) {
                    Intent intent = new Intent(CreateAccountActivity.this, CreateSuccessActivity.class);
                    launcher.launch(intent);
                }
                else{
//
//            Warning Toast: Account or Email is already Exist
//
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

    }
}