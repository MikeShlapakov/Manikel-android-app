package com.example.project_part2.apis;

import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.R;
import com.example.project_part2.entities.Post;
import com.example.project_part2.util.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
//    private MutableLiveData<List<Post>> postListData;
//    private PostDao postDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


    public PostAPI() {
//        this.postListData = postListData;
//        this.dao = dao;

        // Gson will automatically convert Post to json and vice-versa
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get(MutableLiveData<List<Post>> posts) {

        Call<List<Post>> call = webServiceAPI.getPosts();
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                // setting posts of local db to response from server
                posts.setValue(response.body());
//
//                new Thread(() -> {
//                    dao.clear();
//                    dao.insertList(response.body());
//                    postListData.postValue(dao.get());
//                }).start()
            }


            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("nooni");
            }
        });
    }

}
