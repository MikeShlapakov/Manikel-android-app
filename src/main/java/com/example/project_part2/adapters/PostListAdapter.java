package com.example.project_part2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.MainActivity;
import com.example.project_part2.PersonalFeedActivity;
import com.example.project_part2.R;
import com.example.project_part2.entities.Comment;
import com.example.project_part2.entities.Post;
import com.example.project_part2.util.MyApplication;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView authorName;
        private final TextView timestamp;
        private final ImageButton pfp;
        private final ImageView contentImg;
        private final ImageButton likeBtn;
        public final TextView likeCount;
        private final RelativeLayout postLayout;
        private final RecyclerView rvComments;
        private final EditText etNewComment;
        private final ImageButton btnAddComment;

        public PostViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tvContent);
            authorName = itemView.findViewById(R.id.tvAuthor);
            timestamp = itemView.findViewById(R.id.tvTimeStamp);
            pfp = itemView.findViewById(R.id.ivPfp);
            contentImg = itemView.findViewById(R.id.ivContentImage);
            likeBtn = itemView.findViewById(R.id.likeButton);
            likeCount = itemView.findViewById(R.id.tvlikeCount);
            postLayout = itemView.findViewById(R.id.postLayout);
            rvComments = itemView.findViewById(R.id.rvComments);
            etNewComment = itemView.findViewById(R.id.etNewComment);
            btnAddComment = itemView.findViewById(R.id.btnAddComment);
        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;
    public PostListAdapter (Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        if (posts != null) {
            // set Post
            Post current = posts.get(position);

            holder.pfp.setImageURI(current.getAuthor().getPfp());

            holder.pfp.setOnClickListener(item -> {

                PopupMenu popup = new PopupMenu(MyApplication.context, holder.pfp);
                popup.getMenuInflater().inflate(R.menu.post_options_menu, popup.getMenu());

                // TODO: server
//                if (!areFriends(MainActivity.registeredUser, current.getAuthor())) {
//                    MenuItem seePostsItem = popup.getMenu().findItem(R.id.see_posts);
//                    seePostsItem.setEnabled(false);
//                }

                popup.setOnMenuItemClickListener(item1 -> {
                        if (item1.getItemId() == R.id.add_friend) {
                            Toast.makeText(MyApplication.context, "friend request sent", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        else if (item1.getItemId() == R.id.see_posts) {
                            Toast.makeText(MyApplication.context, "entering friend's posts", Toast.LENGTH_SHORT).show();

                            // i want to start the activity but this class isnt of type AppcompatActivity
                            Intent intent = new Intent(MyApplication.context, PersonalFeedActivity.class);
//                            startActivity(intent);
                            mInflater.getContext().startActivity(intent);

                            return true;
                        }
                        return false;
                });

                popup.show();
            });



            String displayName = current.getAuthor().getDisplayName();
            holder.authorName.setText(displayName);
            holder.content.setText(current.getContent());
            holder.timestamp.setText(current.getTimestamp());
//            holder.likeCount.setText(current.getLikeCount());

            // reduce view height if image is missing
            if (current.getImage() == null) {
                holder.contentImg.setVisibility(View.GONE);
            } else {
                holder.contentImg.setImageURI(current.getImage());
                holder.contentImg.setVisibility(View.VISIBLE);
            }


            setLikeColor(current, holder.likeBtn);
            // add on click listener for like button
            holder.likeCount.setText(Integer.toString(current.getLikeCount()));
            holder.likeBtn.setOnClickListener(v -> onLikeClicked(current, holder.likeBtn, holder.likeCount));
//            holder.timestamp.setTag(current.getUrl()); // holds the post URL as String

            // Set up the comments RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
            holder.rvComments.setLayoutManager(layoutManager);

            // Create an adapter for the comments
            // use default user icon for comment author instead of custom user icon.
            CommentAdapter commentAdapter = new CommentAdapter(mInflater, current.getComments());
            holder.rvComments.setAdapter(commentAdapter);

            // Add a new comment
            holder.btnAddComment.setOnClickListener(v -> {
                Comment newComment = new Comment(MainActivity.registeredUser.getPfp(), holder.etNewComment.getText().toString());
                if (!newComment.getContent().isEmpty()) {
                    current.addComment(newComment);
                    commentAdapter.notifyDataSetChanged();
                    holder.etNewComment.getText().clear();
                }
            });

            // add comment listener on content
            holder.content.setOnClickListener((v) -> editPost(position));

            holder.itemView.requestLayout();
            holder.postLayout.requestLayout();
        }
    }

    private void editPost(int position) {
        Post post = posts.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        builder.setTitle("Edit Post");

        // Set up the input
        final EditText input = new EditText(mInflater.getContext());
        // Specify the type of input expected; this, for example, sets the input as a password and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newText = input.getText().toString();
            post.setContent(newText);
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            posts.remove(position);
            notifyDataSetChanged();
        });

        builder.show();
    }

    private void setLikeColor(Post current, ImageButton likeBtn) {
        if (current.getLiked()) {
            likeBtn.setImageResource(R.drawable.like_bold);
        } else {
            likeBtn.setImageResource(R.drawable.like);
        }
    }

    private void onLikeClicked(Post post, ImageButton btn, TextView count) {
        post.like();
        setLikeColor(post, btn);
        count.setText(Integer.toString(post.getLikeCount()));
    }

    @Override
    public int getItemCount() {
        return posts == null? 0 : posts.size();
    }

    public void setPosts(List<Post> s) {
        this.posts = s;
    }
}
