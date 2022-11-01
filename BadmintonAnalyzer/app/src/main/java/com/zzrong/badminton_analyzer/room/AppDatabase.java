package com.zzrong.badminton_analyzer.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Video.class, History.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();   //User Info
    public abstract VideoDao videoDao(); //Video Info
    public abstract HistoryDao historyDao();//Recently viewed video
}
