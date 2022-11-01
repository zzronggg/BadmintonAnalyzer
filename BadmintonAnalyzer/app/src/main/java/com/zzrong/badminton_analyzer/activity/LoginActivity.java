package com.zzrong.badminton_analyzer.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.FlaskApiSender;
import com.zzrong.badminton_analyzer.room.AppDatabase;
import com.zzrong.badminton_analyzer.room.User;
import com.zzrong.badminton_analyzer.room.UserDao;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase database;
    private UserDao userDao;
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
//      has logged -> jump to next page
        checkLogged();
    }

    public void initialize(){
        btn_login = findViewById(R.id.btn_login);
        btn_create_acc = findViewById(R.id.btn_create_submit);
        tv_forgot_pw = findViewById(R.id.tv_forgot_pw);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "info").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao =  database.userDao();
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

    public void checkLogged(){
        if(userDao.getUserCount() > 0){
            intent = new Intent(getApplicationContext(), RecentlyViewActivity.class);
            this.startActivity(intent);
        }
    }

    public void click_login(View view){
        current = view.getContext();

        String acc = ((TextInputLayout)findViewById(R.id.tf_login_username)).getEditText().getText().toString();
        String pw = ((TextInputLayout)findViewById(R.id.tf_login_password)).getEditText().getText().toString();

        if(acc.isEmpty()||pw.isEmpty()){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout),"請填寫每個欄位",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        Runnable r = new Runnable(){
            @Override
            public void run() {
                String response = FlaskApiSender.logIn(acc,pw);
                Log.d("Login_response: ",response);
                if(! response.equals("Fail")){
                    try {
                        //Save userdata into room db
                        JSONObject json = new JSONObject(response);
                        String uName = json.getString("name");
                        String uMail = json.getString("mail");
                        userDao.insert(new User(uName, uMail));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    //Toast: Login Success
                    intent = new Intent(current, RecentlyViewActivity.class);
                    //tag: 通知Rctly Page 要顯示歡迎提示
                    Bundle bundle = new Bundle();
                    bundle.putInt("tag",1);
                    intent.putExtras(bundle);
                    current.startActivity(intent);
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout),
                                    "帳號或密碼有誤",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                }
            }
        };

        Thread t = new Thread(r);
        t.start();


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