package com.example.project_part2.entities;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE _lid = :id")
    User get(long id);

    @Insert
    long[] insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
//
//    @Query("SELECT * FROM post ORDER BY id DESC")
//    MutableLiveData<List<User>> getAll();

    // will need to update version maybe
    @Database(entities = {User.class}, version = 1)
    abstract class UserDB extends RoomDatabase {
        //        private static UserDB instance;
        public abstract UserDao userDao();

//        public static synchronized UserDB getInstance(Context context) {
//            if (instance == null) {
//                instance = Room.databaseBuilder(context.getApplicationContext(),
//                        UserDB.class, "postDB").fallbackToDestructiveMigration().build();
//            } else {
//                return instance;
//            }
//        }
    }
}
