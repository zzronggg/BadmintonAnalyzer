package com.zzrong.badminton_analyzer.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "History")
public class History {
    public History(String vid, String uName) {
        this.vid = vid;
        this.uName = uName;
    }

    @PrimaryKey(autoGenerate = true)
    public int hid;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    @ColumnInfo(name = "vid")
    public String vid;

    @ColumnInfo(name = "user_name")
    public String uName;
}
