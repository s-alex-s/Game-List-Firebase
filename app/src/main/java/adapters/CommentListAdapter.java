package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gamelist.R;

import java.util.ArrayList;

import classes.Comment;

public class CommentListAdapter extends ArrayAdapter<Comment> {
    Context context;
    ArrayList<Comment> comments;

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.item_comment, comments);

        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Comment comment = comments.get(position);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        @SuppressLint({"ViewHolder", "InflateParams"}) View v =
                inflater.inflate(R.layout.item_comment, null, false);

        TextView tvValue = v.findViewById(R.id.comment_value);
        tvValue.setText(comment.getValue());

        v.setTag(comment.getKey());

        return v;
    }
}
