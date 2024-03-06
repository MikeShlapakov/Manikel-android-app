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
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> index();

    @Query("SELECT * FROM post WHERE id = :id")
    Post get(int id);

    @Insert
    void insert(Post... posts);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);
//
//    @Query("SELECT * FROM post ORDER BY id DESC")
//    MutableLiveData<List<Post>> getAll();

    // will need to update version maybe
    @Database(entities = {Post.class}, version = 1)
    abstract class PostDB extends RoomDatabase {
//        private static PostDB instance;
        public abstract PostDao postDao();

//        public static synchronized PostDB getInstance(Context context) {
//            if (instance == null) {
//                instance = Room.databaseBuilder(context.getApplicationContext(),
//                        PostDB.class, "postDB").fallbackToDestructiveMigration().build();
//            } else {
//                return instance;
//            }
//        }
    }
}