package com.example.project_part2.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.project_part2.R;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
    private static JSONObject parseJsonFromResRaw(Resources res, int rawId) throws IOException, JSONException {

        InputStream inputStream = res.openRawResource(rawId);
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String jsonString = scanner.next();
        inputStream.close();

        // remove EOL from json files
        jsonString = jsonString.replaceAll("\\r\\n", "");
        // parse the modified JSON to JSONObject
        return new JSONObject(jsonString);
    }

    public static List<Post> samplePostList(Context context) throws JSONException, IOException {

        List<Post> postList = new ArrayList<>();
        Resources res = context.getResources();
        List<Integer> jsonIds = new ArrayList<>();

        // add default posts to list
        jsonIds.add(R.raw.post1);
        jsonIds.add(R.raw.post2);
        jsonIds.add(R.raw.post3);
        jsonIds.add(R.raw.post4);
        jsonIds.add(R.raw.post5);
        jsonIds.add(R.raw.post6);
        jsonIds.add(R.raw.post7);
        jsonIds.add(R.raw.post8);
        jsonIds.add(R.raw.post9);
        jsonIds.add(R.raw.post10);

        for (int id : jsonIds) {
            JSONObject jsonObject = parseJsonFromResRaw(res, id);

            Post post = new Post(jsonObject);
            postList.add(post);
        }

        return postList;
    }
}

