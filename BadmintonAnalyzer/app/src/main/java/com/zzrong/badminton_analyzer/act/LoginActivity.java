package com.zzrong.badminton_analyzer.act;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.zzrong.badminton_analyzer.R;

public class LoginActivity extends AppCompatActivity {

    private Context current;
    private Intent intent;
    private Button btn_login;
    private Button btn_create_acc;
    private TextView tv_forgot_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        applyListener();
    }

    public void initialize(){
        btn_login = findViewById(R.id.btn_login);
        btn_create_acc = findViewById(R.id.btn_create_acc);
        tv_forgot_pw = findViewById(R.id.tv_forgot_pw);
    }

    public void applyListener(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_login(view);
            }
        });

        btn_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_create_acc(view);
            }
        });

        tv_forgot_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_forgot_pw(view);
            }
        });
    }

    public void click_login(View view){
        current = view.getContext();
        intent = new Intent(current, RecentlyViewActivity.class);
        current.startActivity(intent);
    }

    public void click_forgot_pw(View view){
        current = view.getContext();
        intent = new Intent(current, ResetPWActivity.class);
        current.startActivity(intent);
    }

    public void click_create_acc(View view){
        current = view.getContext();
        intent = new Intent(current, CreateAccountActivity.class);
        current.startActivity(intent);
    }
}