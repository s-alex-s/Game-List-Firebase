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

import classes.Game;

public class GameListAdapter extends ArrayAdapter<Game> {
    Context context;
    ArrayList<Game> games;

    public GameListAdapter(Context context, ArrayList<Game> games) {
        super(context, R.layout.item_games, games);

        this.context = context;
        this.games = games;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Game game = this.games.get(position);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        @SuppressLint("ViewHolder") View v =
                inflater.inflate(R.layout.item_games, parent, false);

        TextView tvName = v.findViewById(R.id.game_name);
        TextView tvDate = v.findViewById(R.id.game_date);
        TextView tvGenre = v.findViewById(R.id.game_genre);

        tvName.setText(game.getName());
        tvDate.setText(game.getRelease_date() + "");
        tvGenre.setText(game.getGenre());

        v.setTag(game.getKey());

        return v;
    }
}
