package com.zzrong.badminton_analyzer.func;

public class VideoItem {

    private String id;
    private String title;
    private String thumbnails;
    private String description;

    public VideoItem(String id, String tit, String thumb, String des){
        this.id = id;
        this.title = tit;
        this.thumbnails = thumb;
        this.description = des;
    }

    public String getID(){ return this.id;}
    public String getTitle(){ return this.title;}
    public String getPic(){ return this.thumbnails;}
    public String getDes(){ return this.description;}
}
