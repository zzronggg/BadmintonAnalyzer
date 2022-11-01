package com.zzrong.badminton_analyzer.room;

import androidx.room.*;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM video")
    List<Video> getAll();

    @Query("SELECT * FROM video WHERE id IN (:vIds)")
    List<Video> loadAllByIds(int[] vIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Video video);

//    @Delete
//    void delete(Video video);
}
