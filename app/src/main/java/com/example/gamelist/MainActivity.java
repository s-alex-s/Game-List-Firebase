package com.example.gamelist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.GameListAdapter;
import classes.Game;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db_ref = database.getReference();
    DatabaseReference push_game = db_ref.child("Games").push();

    ArrayList<Game> games;
    ListView listView;
    GameListAdapter gameListAdapter;
    Intent intent;
    public final int OBJECT_ADD = 3;

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == OBJECT_ADD) {
                        assert result.getData() != null;
                        String name = result.getData().getStringExtra("name");
                        int date = Integer.parseInt(result.getData().getStringExtra("date"));
                        String genre = result.getData().getStringExtra("genre");

                        Game g = new Game(name, date, genre);
                        g.setKey(push_game.getKey());
                        push_game.setValue(g);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.game_list);
        games = new ArrayList<>();
        gameListAdapter = new GameListAdapter(this, games);
        listView.setAdapter(gameListAdapter);

        db_ref.child("Games").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                games.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Game game_data = data.getValue(Game.class);
                    games.add(game_data);
                }
                gameListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database load error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_item_option) {
            intent = new Intent(this, AddItemActivity.class);
            intent.putExtra("action", "add");
            activityResult.launch(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}