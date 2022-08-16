package com.zzrong.badminton_analyzer.func;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;


public class Search {

    private ArrayList<String> video_id;
    private ArrayList<String> title;
    private ArrayList<String> thumbnail;
    private ArrayList<String> description;
    final private String API_KEY="AIzaSyAeiu7yY3tVB5_uadtdkTYxoT1KkCjC7ds";

    //connect youtube api and retrieve JSON file
    public void fetch(String keyword){
        video_id = new ArrayList<>();
        title = new ArrayList<>();
        thumbnail = new ArrayList<>();
        description = new ArrayList<>();
        int res_limit = 20;
        Log.d("L",keyword.replace(" ","").length()+"");
        if( keyword.replace(" ","").length() > 0){
            keyword = "+" + keyword.trim().replace(" ","+");
        }
        else keyword = "";

        String kw = "bwf" + keyword;
        Log.d("keywords: ",kw);

        String url = null;
        String order = "relevance"; //date, rating, relevance, title, videoCount,viewCount
        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults="+res_limit+
                "&order="+order+"&q="+kw+"&key="+API_KEY;
        Log.d("Query: ",url);

        try {
            String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
            parse(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Thread: ", "END");
    }

    //parse JSON file and save video id, title, thumb to this object's ArrayList
    protected void parse(String json) throws JSONException {
        //Make a object
        JSONObject ob = new JSONObject(json);
        String vid="";
        String tit="";
        String thumb="";
        String des="";
        String token=""; //next page , under construction
        for(int i = 0; i< ob.getJSONArray("items").length(); i++){

            try {
                vid = ob.getJSONArray("items").getJSONObject(i).getJSONObject("id").getString("videoId");
                tit = ob.getJSONArray("items").getJSONObject(i).getJSONObject("snippet").getString("title");
                thumb = ob.getJSONArray("items").getJSONObject(i).getJSONObject("snippet").
                        getJSONObject("thumbnails").getJSONObject("high").getString("url");
                des = ob.getJSONArray("items").getJSONObject(i).getJSONObject("snippet").getString("description");

                thumb.replace("hqdefault","maxresdefault");
            }
            catch(JSONException e){
                continue;
            }
            video_id.add(vid);
            title.add(tit);
            thumbnail.add(thumb);
            description.add(des);
        }

    }

    //Search Fragment will get all information from here
    public ArrayList<String> getId(){
        Log.d("ARRAY: ",video_id.size()+"");
        return video_id;
    }

    public ArrayList<String> getTitle(){
        return title;
    }

    public ArrayList<String> getThumbnail(){
        return thumbnail;
    }

    public ArrayList<String> getDescription(){ return description;}
}
