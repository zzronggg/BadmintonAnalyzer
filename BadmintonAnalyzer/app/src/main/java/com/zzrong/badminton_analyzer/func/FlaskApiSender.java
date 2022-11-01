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

    public static String logIn(String acc, String pw){
        String api = "/log_in";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", acc);
            jsonObject.put("password", pw);
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
        String msg = null;
        try {
            response = client.newCall(request).execute();
            msg = response.body().string();
            Log.d("response: ",msg);
            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Boolean signUp(String acc, String mail, String pw){
        String api = "/sign_up";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", acc);
            jsonObject.put("password", pw);
            jsonObject.put("email", mail);
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
        String msg = null;
        try {
            response = client.newCall(request).execute();
            msg = response.body().string();
            Log.d("response: ",msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(msg.equals("Aborted")){
            return false;
        }
        return true;

    }

    public static String addBookmark(String user, String bookmark){

        //add (user,bookmark) record on server database
        String api = "/add_bookmark";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
            jsonObject.put("bookmark", bookmark);
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
            return resStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
public static String getBookmark(String user){

        //get user's bookmark (JSON String) from server database
        String api = "/get_bookmark";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
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

        try {
            Response response = client.newCall(request).execute();
            String msg = response.body().string();
            Log.d("response: ",msg);
            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeBookmark(String user, String bookmark){

    }

    public static String getBookmarkContent(String bookmarkId){
        //get user's bookmark content from server database
        String api = "/get_bookmark_content";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", bookmarkId);
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

        try {
            Response response = client.newCall(request).execute();
            String msg = response.body().string();
            Log.d("response: ",msg);
            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addBookmarkContent(String bookmarkId, String vid){

        //add (user,bookmark) record on server database
        String api = "/add_bookmark_content";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bookmark", bookmarkId);
            jsonObject.put("video", vid);
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
//            return resStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return null;
    }

    public static void updateBookmarkContent(String bookmarkId, String newBookmarkId, String vid){

        //add (user,bookmark) record on server database
        String api = "/update_bookmark_content";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bookmark", bookmarkId);
            jsonObject.put("newBookmark", newBookmarkId);
            jsonObject.put("video", vid);
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
//            return resStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return null;
    }

    public static String removeBookmarkContent(String bookmarkId, String vid){

        //add (user,bookmark) record on server database
        String api = "/remove_bookmark_content";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bookmark", bookmarkId);
            jsonObject.put("video", vid);
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
            return resStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFavorite(String user){
        //get user's bookmark content from server database
        String api = "/get_favorite";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
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

        try {
            Response response = client.newCall(request).execute();
            String msg = response.body().string();
            Log.d("response: ",msg);
            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addVid(String vid, String title){

        //download vid to server
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

    public static String getVideo(String vid){
        //get user's bookmark content from server database
        String api = "/get_video";
        Log.d("POST",url + api);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient().newBuilder().addInterceptor(logging).build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", vid);
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

        try {
            Response response = client.newCall(request).execute();
            String msg = response.body().string();
            Log.d("response: ",msg);
            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



//    public static ArrayList<String> getSectPred(String vid, String section){
//        //underconstruction
//        return VideoItemSample.sectFragSample();
//    }
    public static ArrayList<String> getSectStat(String vid, String section){
        //underconstruction
        return SampleProvider.statFragSample();
    }
}
