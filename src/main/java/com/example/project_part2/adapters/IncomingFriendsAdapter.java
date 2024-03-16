package com.example.project_part2.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.Friend;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.ImageUtil;
import com.example.project_part2.util.MyApplication;

import java.util.List;

public class IncomingFriendsAdapter extends BaseAdapter {

    private Context context;
    private MutableLiveData<List<Friend>> incoming;
    private UserAPI userAPI;

    public IncomingFriendsAdapter(Context context, List<Friend> incoming) {
        super();
        this.context = context;
        this.incoming = new MutableLiveData<>();
        this.incoming.setValue(incoming);
        userAPI = new UserAPI();
    }
    @Override
    public int getCount() {
        return incoming.getValue().size();
    }

    @Override
    public Object getItem(int position) {
        return incoming.getValue().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.incoming_friend, parent, false);
        }

        Friend f;
        // if didn't get any posts.
        if (incoming.getValue() == null) { return null; }
        else { f = incoming.getValue().get(position); }

        ImageView pfpImage = convertView.findViewById(R.id.pfp);
        TextView fullName = convertView.findViewById(R.id.fullname);

//        TextView time = convertView.findViewById(R.id.time);
        ImageButton acceptButton = convertView.findViewById(R.id.accept);
        ImageButton denyButton = convertView.findViewById(R.id.deny);

        if (!f.getPfp().equals("")) {
            pfpImage.setImageURI(ImageUtil.decodeBase64ToUri(f.getPfp(), MyApplication.context)); }

        fullName.setText(f.getDisplayName());

        acceptButton.setOnClickListener(item -> {
            userAPI.acceptFriendRequest(f.getId(), incoming, position, this);
//            acceptFriend(MainActivity.registeredUser, user);
            // delete this from list
        });

        denyButton.setOnClickListener(item -> {
            userAPI.deleteFriendRequest(f.getId(), incoming, position, this);
        });

        return convertView;
    }
}
