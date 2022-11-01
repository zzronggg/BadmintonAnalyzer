package com.zzrong.badminton_analyzer.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "User")
public class User {

    public User(String name, String mail){
        this.name = name;
        this.mail = mail;
    }

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "mail")
    public String mail;

    @NotNull
    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }
}
