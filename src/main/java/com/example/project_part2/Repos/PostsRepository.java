package com.example.project_part2.Repos;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.PostDao;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {

//    private static MutableLiveData<List<Post>> posts;
    private PostDao postDao;
    private static PostListData postListData;
    private PostAPI api;


    // who initializes this class?
    public PostsRepository() {
//        PostDao.PostDB db = PostDao.PostDB.getInstance(application);
//        postDao = db.postDao();
//        posts = postDao.getAll();
//        PostDao.PostDB db = Room.databaseBuilder(MyApplication.context,
//                        PostDao.PostDB.class, "PostDB")
//                .       allowMainThreadQueries().build();;

//        postDao = db.postDao();
        postListData = new PostListData();
//        api = new PostAPI(postListData, postDao);
//        api = new PostAPI();

    }

    public static MutableLiveData<List<Post>> getPosts() {
        return postListData;
    }



    class PostListData extends MutableLiveData<List<Post>> {

        public PostListData () {
            super();

            new Thread (() -> {

                List<Post> posts = new LinkedList<>();
                // observers will get updated from these changes
                posts.add(new Post("lorem lorem", null, new User("hello", "hello", null)));
                posts.add(new Post("lorem lorem2", null, new User("hello2", "hello2", null)));
                posts.add(new Post("lorem lorem3", null, new User("hello3", "hello3", null)));
//                setValue(posts);

                // "this" will have the value of posts
                postValue(posts);
//                setValue(posts);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("done sleepin");
                PostAPI postAPI = new PostAPI();
                postAPI.get(this);

            }).start();
        }

        @Override
        protected void onActive() {
            super.onActive();
//
//            new Thread (() -> {
//                posts.postValue(dao.get());
//            }).start();
        }

        public LiveData<List<Post>> getAll() {
            return postListData;
        }

//        public void add (final Post post) {
//            api.add(post);
//        }
    }
}
