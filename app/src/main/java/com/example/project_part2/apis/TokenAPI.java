package com.example.project_part2.apis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.R;
import com.example.project_part2.entities.BodyRequests.TokenBody;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public TokenAPI() {

        // Gson will automatically convert Post to json and vice-versa
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void createToken(String id, MutableLiveData<String> tokenLiveData) {

        TokenBody tokenBody = new TokenBody(id);

        Call<String> call = webServiceAPI.createToken(tokenBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response");
                if (response.isSuccessful()) {
                    tokenLiveData.postValue(response.body());
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("failure");
            }
        });
    }

    public void isLoggedIn(MutableLiveData<Boolean> booleanMutableLiveData, String token) {

        Call<Boolean> call = webServiceAPI.isLoggedIn(token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    booleanMutableLiveData.postValue(response.body());
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure
            }
        });
    }


}
