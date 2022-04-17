package com.example.gamelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.CommentListAdapter;
import classes.Comment;

public class CommentActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference comm_ref = database.getReference().child("Game_comments");
    DatabaseReference push;

    Intent intent;
    ArrayList<Comment> comments;
    CommentListAdapter commentListAdapter;
    ListView listView;
    LinearLayout comment_form;
    TextView text;
    ProgressBar progressBar;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        listView = findViewById(R.id.list_comments);
        progressBar = findViewById(R.id.progressBar_comment);
        comment_form = findViewById(R.id.comment_form);
        text = findViewById(R.id.comment_edit_text);

        comments = new ArrayList<>();
        commentListAdapter = new CommentListAdapter(this, comments);
        listView.setAdapter(commentListAdapter);

        arguments = getIntent().getExtras();

        comm_ref.orderByChild("game_key").equalTo(arguments.getString("game_key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    comments.add(data.getValue(Comment.class));
                }

                progressBar.setVisibility(View.GONE);
                comment_form.setVisibility(View.VISIBLE);
                commentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Database load error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clicked_save(View v) {
        if (!text.getText().toString().replace(" ", "").equals("")) {
            push = comm_ref.push();

            Comment comment = new Comment(text.getText().toString());
            comment.setKey(push.getKey());
            comment.setGame_key(arguments.getString("game_key"));

            push.setValue(comment);

            text.setText("");
        } else {
            text.setError("Incorrect input");
        }
    }

    public void clicked_cancel(View v) {
        finish();
    }
}