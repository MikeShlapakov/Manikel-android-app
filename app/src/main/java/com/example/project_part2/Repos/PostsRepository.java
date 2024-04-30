package com.example.project_part2.Repos;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_part2.MainActivity;
import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.entities.Post;
//import com.example.project_part2.entities.PostDao;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {

//    private static MutableLiveData<List<Post>> posts;
//    private PostDao postDao;
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

                String defaultImg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAYAAAA5ZDbSAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAeKADAAQAAAABAAAAeAAAAAAI4lXuAAAC8UlEQVR4Ae2dQU7sQAwFAUX6a4401+IAHONfZc7FChZsGXUWXXm2p1gGy89dFaPRKAqv/9/fv1/8eUjg6/Pzz9/dbrc/r1e7+FZtIOfZS0DBe3mW66bgckr2DqTgvTzLdVNwOSV7B1LwXp7luim4nJK9Ayl4L89y3RRcTsnegRS8l2e5bgoup2TvQMej71r3xpzv9u/j43yxlUsCbvASUe8CBff2t5xewUtEvQsU3NvfcnoFLxH1LjhSTybc7/fW5LrM7wa3vs3Wwyt4zah1hYJb61sPr+A1o9YVCm6tbz38sS557oru3427wcPvXwUreDiB4cdzgxU8nMDw47nBCh5OYPjx3GAFDycw/HhusIKHExh+PDdYwcMJDD+eG6zg4QSGH88NVvBwAsOPd3R5vne4B+x4/onG0NZorOAaHrApFIyhrdFYwTU8YFMoGENbo/Gr74uuIYKawg2myBbpq+AiIqgxFEyRLdJXwUVEUGMomCJbpK+Ci4igxlAwRbZIXwUXEUGNoWCKbJG+Ci4ighpDwRTZIn3HvqOj2nuwU77d4BT5i3IVfBHoVIyCU+QvylXwRaBTMQpOkb8od+yn6Ef8Uu/HfjQPfd0NpgmH+ys4LICOVzBNONxfwWEBdLyCacLh/goOC6DjFUwTDvdXcFgAHa9gmnC4v4LDAuh4BdOEw/0VHBZAxyuYJhzur+CwADpewTThcH8FhwXQ8QqmCYf7H6nnh7v/T8Cwt9PxbvBpVD0LFdzT2+mpFXwaVc9CBff0dnpqBZ9G1bPwoJ8T9n3U2RvDDc7yx9MVjCPOBig4yx9PVzCOOBug4Cx/PF3BOOJsgIKz/PF0BeOIswEKzvLH0xWMI84GKDjLH09/und0PNt3424wvkPZAAVn+ePpCsYRZwMUnOWPpysYR5wNGPsp2ueuf28sNzi7YHi6gnHE2QAFZ/nj6QrGEWcDFJzlj6crGEecDVBwlj+ermAccTZAwVn+eLqCccTZAAVn+ePpP4tDHr4SKVqVAAAAAElFTkSuQmCC";

                // observers will get updated from these changes
                posts.add(new Post("lorem lorem", "", "", defaultImg, "grandimama"));
                posts.add(new Post("lorem lorem2", "", "", defaultImg, "fufi"));
                posts.add(new Post("lorem lorem3", "", "", defaultImg, "mome"));

                postValue(posts);

//              // "this" will have the value of posts
//                postValue(posts);
//                setValue(posts);

//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                System.out.println("done sleepin");
                PostAPI postAPI = new PostAPI();
                postAPI.getFeedPosts(this);

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
