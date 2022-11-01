package com.zzrong.badminton_analyzer.func;

public class VideoItem {

    private String id;
    private String title;
    private String thumbnails;
    private String status;

    public VideoItem(String id, String tit, String thumb, String status){
        this.id = id;
        this.title = tit;
        this.thumbnails = thumb;
        this.status = status;
    }

    public VideoItem(String id, String tit, String status){
        this.id = id;
        this.title = tit;
        this.thumbnails = "https://i.ytimg.com/vi/" + id + "/mqdefault.jpg";
        this.status = status;
    }

    public String getID(){ return this.id;}
    public String getTitle(){ return this.title;}
    public String getPic(){ return this.thumbnails;}
    public String getStatus(){ return this.status;}
}
