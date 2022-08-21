package com.zzrong.badminton_analyzer.func;

import android.util.Log;
import com.zzrong.badminton_analyzer.BuildConfig;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class FlaskApiSender {

    private static OkHttpClient client;
    private static final String url = BuildConfig.API_CALL;


    public FlaskApiSender(){

    }

    public void signUp(){

    }

    public static void addVid(String vid, String title){

        String api = "/download";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", vid);
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

    public static ArrayList<String> getSectPred(String vid, String section){
        //underconstruction
        return VideoItemSample.predFragSample();
    }
    public static ArrayList<String> getSectStat(String vid, String section){
        //underconstruction
        return VideoItemSample.statFragSample();
    }
}
