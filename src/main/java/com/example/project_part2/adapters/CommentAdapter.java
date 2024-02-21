package com.example.project_part2.adapters;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_part2.R;
import com.example.project_part2.entities.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<String> comments;
    private final LayoutInflater mInflater;

    private final int author_pfp;

    public CommentAdapter(LayoutInflater inflater, List<String> comments, int author_pfp) {
        mInflater = inflater;
        this.comments = comments;
        this.author_pfp = author_pfp;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        ImageView fd = itemView.findViewById(R.id.ivProfileComment);
        fd.setImageResource(author_pfp);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // set the comment text
        String comment = comments.get(position);
        holder.tvComment.setText(comment);

        // set the btnEditComment onClick
        holder.btnEditComment.setOnClickListener((v)-> editComment(position));
    }

    private void editComment(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        builder.setTitle("Edit Comment");

        // Set up the input
        final EditText input = new EditText(mInflater.getContext());
        // Specify the type of input expected; this, for example, sets the input as a password and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newText = input.getText().toString();
            comments.set(position, newText);
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            comments.remove(position);
            notifyDataSetChanged();
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvComment;
        private final ImageButton btnEditComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            btnEditComment = itemView.findViewById(R.id.btnEditComment);
        }
    }
}
