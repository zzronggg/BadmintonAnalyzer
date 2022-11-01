package com.zzrong.badminton_analyzer.room;

import androidx.room.*;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Video")
public class Video {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @NotNull
    public String id;

    public Video(String id, String title, String pic, String date) {
        this.id =  id;
        this.title = title;
        this.pic = pic;
        this.date = date;
    }

    @ColumnInfo(name = "thumbnail")
    public String pic;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name= "AddDate")
    public String date;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPic(){
        return pic;
    }

    public String getDate() {
        return date;
    }
}