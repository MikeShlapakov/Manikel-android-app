package com.example.project_part2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.MainActivity;
import com.example.project_part2.PersonalFeedActivity;
import com.example.project_part2.R;
import com.example.project_part2.apis.PostAPI;
import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.Post;
import com.example.project_part2.entities.User;
import com.example.project_part2.util.ImageUtil;
import com.example.project_part2.util.MyApplication;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    public class PostViewHolder extends RecyclerView.ViewHolder {
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
    public UserAPI userAPI = new UserAPI();
    private final PostAPI postAPI = new PostAPI();


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


            // TODO
//            MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
//            userAPI.getUserById(userMutableLiveData, current.getAuthorId());
//            User u = userMutableLiveData.getValue();

            if (current.getAuthorPfp() != null ) { holder.pfp.setImageURI(Uri.parse(current.getAuthorPfp())); }

            Uri u = ImageUtil.decodeBase64ToUri(current.getImage(), MyApplication.context);
            // reduce view height if image is missing
            if (current.getImage().equals("") || u == null) {
                holder.contentImg.setVisibility(View.GONE);
            } else {
                holder.contentImg.setImageURI(null);
                holder.contentImg.setImageURI(u);
                holder.contentImg.setVisibility(View.VISIBLE);
            }

            holder.authorName.setText(current.getAuthorDisplayName());
            holder.content.setText(current.getContent());
            holder.timestamp.setText(current.getDate());

            // reduce view height if image is missing
            if (!current.getAuthorPfp().equals("")) {
                holder.pfp.setImageURI(ImageUtil.decodeBase64ToUri(current.getAuthorPfp(), MyApplication.context));
                holder.pfp.setVisibility(View.VISIBLE);
            }

            setLikeColor(current, holder.likeBtn);
            // add on click listener for like button
            holder.likeCount.setText(Integer.toString(current.getLikeCount()));
            holder.likeBtn.setOnClickListener(v -> onLikeClicked(current, holder.likeBtn, holder.likeCount));

            // Set up the comments RecyclerView
//            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
//            holder.rvComments.setLayoutManager(layoutManager);

            // Create an adapter for the comments
            // use default user icon for comment author instead of custom user icon.
//            CommentAdapter commentAdapter = new CommentAdapter(mInflater, current.getComments());
//            holder.rvComments.setAdapter(commentAdapter);

            // BONUS: Add a new comment
//            holder.btnAddComment.setOnClickListener(v -> {
//                Comment newComment = new Comment(MainActivity.registeredUser.getPfp(), holder.etNewComment.getText().toString());
//                if (!newComment.getContent().isEmpty()) {
//                    current.addComment(newComment);
//                    commentAdapter.notifyDataSetChanged();
//                    holder.etNewComment.getText().clear();
//                }
//            });

            // add comment listener on content
            holder.content.setOnClickListener( item -> {
                if (current.getAuthorId().equals(MyApplication.registeredUser.getValue().id())) {
                    editPost(position);
                }
            });

            // clickable pfp
            holder.pfp.setOnClickListener(item -> {

                if (!holder.authorName.getText().toString().equals(MyApplication.registeredUser.getValue().getDisplayName())) {

                    PopupMenu popup = new PopupMenu(MyApplication.context, holder.pfp);
                    popup.getMenuInflater().inflate(R.menu.post_options_menu, popup.getMenu());
                    MenuItem addFriendItem = popup.getMenu().findItem(R.id.add_friend);

                    if (!userAPI.isFriend(MyApplication.registeredUser.getValue(), current.getAuthorId())) {
                        // if is not a friend
                        MenuItem seePostsItem = popup.getMenu().findItem(R.id.see_posts);
                        seePostsItem.setEnabled(false);
                    } else {
                        // if is a friend
                        MenuItem seePostsItem = popup.getMenu().findItem(R.id.see_posts);

                        addFriendItem.setTitle("Delete Friend");
//                        addFriendItem.setEnabled(false);
                        seePostsItem.setEnabled(true);
                    }

                    popup.setOnMenuItemClickListener(item1 -> {

                        // if clicking on myself then nothing
                        if (item1.getItemId() == R.id.add_friend) {
                            if (addFriendItem.getTitle().toString().equals("Delete Friend")) {
                                userAPI.deleteFriend(current.getAuthorId());
                            } else {
                                userAPI.sendFriendRequest(current.getAuthorId());
                            }
                            return false;
                        }
                        else if (item1.getItemId() == R.id.see_posts) {
//                            Toast.makeText(MyApplication.context, "entering friend's posts", Toast.LENGTH_SHORT).show();

                            // i want to start the activity but this class isn't of type AppcompatActivity
                            Intent intent = new Intent(MyApplication.context, PersonalFeedActivity.class);
//                            startActivity(intent);
                            intent.putExtra("DISPLAY_NAME", current.getAuthorDisplayName());
                            intent.putExtra("PFP", current.getAuthorPfp());
                            intent.putExtra("ID", current.getAuthorId());
                            mInflater.getContext().startActivity(intent);

                            return true;
                        }
                        return false;
                    });

                    popup.show();

                } else {

                }
            });

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
            postAPI.editPost(post, input, this);;
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            postAPI.deletePost(posts, position, this);
        });

        builder.show();
    }

    private void setLikeColor(Post current, ImageButton likeBtn) {
        if (current.isLiked()) {
            likeBtn.setImageResource(R.drawable.like_bold);
        } else {
            likeBtn.setImageResource(R.drawable.like);
        }
    }

    private void onLikeClicked(Post post, ImageButton btn, TextView count) {
        post.like();
        setLikeColor(post, btn);
        count.setText(Integer.toString(post.getLikeCount()));
        postAPI.likePost(post);
    }

    @Override
    public int getItemCount() {
        return posts == null? 0 : posts.size();
    }

    public void setPosts(List<Post> s) {
        this.posts = s;
    }
}
