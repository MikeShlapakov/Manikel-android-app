package com.example.project_part2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;
import com.example.project_part2.entities.User;

import java.util.List;

public class IncomingFriendsAdapter extends BaseAdapter {

    private Context context;
    private List<User> incoming;

    public IncomingFriendsAdapter(Context context, List<User> incoming) {
        super();
        this.context = context;
        this.incoming = incoming;
    }
    @Override
    public int getCount() {
        return incoming.size();
    }

    @Override
    public Object getItem(int position) {
        return incoming.get(position);
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

        User user = incoming.get(position);

        ImageView pfpImage = convertView.findViewById(R.id.pfp);
        TextView fullName = convertView.findViewById(R.id.fullname);
        // TODO: make a new server query for incoming user request
//        TextView time = convertView.findViewById(R.id.time);
        ImageButton acceptButton = convertView.findViewById(R.id.accept);
        ImageButton denyButton = convertView.findViewById(R.id.deny);

        pfpImage.setImageURI(user.getPfp());
        fullName.setText(user.getDisplayName());

        acceptButton.setOnClickListener(item -> {
            // TODO: bonus
//            acceptFriend(MainActivity.registeredUser, user);
            // delete this from list
        });

        denyButton.setOnClickListener(item -> {
            // TODO: bonus
        });
        return convertView;
    }
}
