package com.example.project_part2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.R;
import com.example.project_part2.entities.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent;
        private final TextView tvAuthor;
        private final TextView tvTimeStamp;
        private final ImageView ivPicture;
        private final ImageView ivAuthorPicture;
        private final ImageButton likeBtn;
        private final RelativeLayout postLayout;
        private final RecyclerView rvComments;
        private final EditText etNewComment;
        private final ImageButton btnAddComment;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            ivAuthorPicture = itemView.findViewById(R.id.ivAuthorPicture);
            likeBtn = itemView.findViewById(R.id.likeButton);
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
            Post current = posts.get(position);
            String firstName = current.getAuthor().getFirstName();
            String lastName = current.getAuthor().getLastName();
            holder.tvAuthor.setText(firstName.concat(" ").concat(lastName));
            holder.tvContent.setText(current.getText());
            holder.tvTimeStamp.setText(current.getTimeStamp());
            // reduce view height if image is missing
            if (current.getImage() == null) {
                holder.ivPicture.setVisibility(View.GONE);
            } else {
                holder.ivPicture.setImageURI(current.getImage());
                holder.ivPicture.setVisibility(View.VISIBLE);
            }
            if (current.getAuthor().getProfilePictureUri() != null) {
//                holder.ivAuthorPicture.setImageURI(current.getAuthor().getProfilePictureUri());
                holder.ivAuthorPicture.setImageResource(Integer.parseInt(current.getAuthor().getProfilePictureUri().toString()));
            } else {
                holder.ivAuthorPicture.setImageResource(R.drawable.default_profile_pic);
            }
            setLikeColor(current, holder.likeBtn);
            // add on click listener for like button
            holder.likeBtn.setOnClickListener(v -> onLikeClicked(current, holder.likeBtn));
            holder.tvTimeStamp.setTag(current.getUrl()); // holds the post URL as String

            // Set up the comments RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
            holder.rvComments.setLayoutManager(layoutManager);

            // Create an adapter for the comments
            CommentAdapter commentAdapter = new CommentAdapter(mInflater, current.getComments(), Integer.parseInt(current.getAuthor().getProfilePictureUri().toString()));
            holder.rvComments.setAdapter(commentAdapter);

            // Add a new comment
            holder.btnAddComment.setOnClickListener(v -> {
                String newComment = holder.etNewComment.getText().toString();
                if (!newComment.isEmpty()) {
                    current.addComment(newComment);
                    commentAdapter.notifyDataSetChanged();
                    holder.etNewComment.getText().clear();
                }
            });

            // add comment listener on tvContent
            holder.tvContent.setOnClickListener((v) -> editPost(position));

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
            post.setText(newText);
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

    private void onLikeClicked(Post post, ImageButton btn) {
        post.toggleLiked();
        setLikeColor(post, btn);
    }

    @Override
    public int getItemCount() {
        return posts == null? 0 : posts.size();
    }

    public void setPosts(List<Post> s) {
        this.posts = s;
    }
}
