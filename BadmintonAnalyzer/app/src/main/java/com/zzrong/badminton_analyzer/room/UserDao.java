package com.zzrong.badminton_analyzer.room;

import androidx.room.*;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    User getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User...users);

    @Query("SELECT count(name) FROM user")
    int getUserCount();
    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();


//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);

//    User findByUID(String uid);

//    @Insert
//    void insertAll(User... users);


}
