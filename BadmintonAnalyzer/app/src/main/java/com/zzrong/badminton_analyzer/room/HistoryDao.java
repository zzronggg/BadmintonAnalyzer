package com.zzrong.badminton_analyzer.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history")
    List<History> getAll();

    @Query("SELECT count(hid) FROM history WHERE user_name = :username")
    int getUserHistoryCount(String username);

    //檢查最新一筆
    @Query("SELECT * FROM history ORDER BY hid DESC")
    History getLatest();

    @Query("DELETE FROM history WHERE vid = :vid AND user_name = :username")
    void delDuplicated(String vid, String username);

    //刪除最舊n筆
    @Query("DELETE FROM history WHERE hid IN " +
            "(SELECT hid FROM history WHERE user_name = :username LIMIT :n)")
    void delRecord(int n, String username);

    //根據五筆history回傳videos
    @Query("SELECT * FROM video WHERE id IN " +
            "(SELECT vid FROM history WHERE user_name = :username ORDER BY hid DESC)")
    List<Video> getVideos(String username);
    @Insert
    void insert(History history);

    @Delete
    void delete(History history);
}
