package com.zzrong.badminton_analyzer.func;

import android.util.Log;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FlaskApiSender {

    private OkHttpClient client;
    private String id;
    private String title;
    private String url;

    public FlaskApiSender(String vid, String title){
        id = vid;
        this.title = title;
        url = "http://140.119.19.35:8887";
    }

    public void signUp(){

    }

    public void addVid(){

        String api = "/download";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON",jsonObject.toString());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Log.d("url: ",url+api);
        Request request = new Request.Builder()
                .url(url + api)
                .post(body) // 使用post連線
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.d("response: ",resStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void checkStat(){

    }
}
