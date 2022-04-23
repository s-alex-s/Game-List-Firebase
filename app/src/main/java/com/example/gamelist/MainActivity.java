package com.example.gamelist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import adapters.GameListAdapter;
import classes.Auth;
import classes.Game;
import classes.User;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db_ref = database.getReference();
    DatabaseReference push;

    ArrayList<Game> games;
    ListView listView;
    GameListAdapter gameListAdapter;
    Intent intent;
    TextView textView;

    Auth auth;
    User user;

    public final int OBJECT_ADD = 3;
    public final int OBJECT_EDIT = 4;
    public final int OBJECT_DELETE = 5;

    ProgressBar progressBar;

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == OBJECT_ADD) {
                        games.clear();
                        gameListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.VISIBLE);

                        assert result.getData() != null;
                        String name = result.getData().getStringExtra("name");
                        int date = Integer.parseInt(result.getData().getStringExtra("date"));
                        String genre = result.getData().getStringExtra("genre");

                        Game g = new Game(name, date, genre);
                        push = db_ref.child("Games").push();
                        g.setKey(push.getKey());
                        push.setValue(g);
                    } else if (result.getResultCode() ==  OBJECT_EDIT) {
                        games.clear();
                        gameListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.VISIBLE);

                        assert result.getData() != null;
                        Game game_obj = (Game) result.getData().getSerializableExtra("game_obj");

                        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                db_ref.child("Games").child(game_obj.getKey()).setValue(game_obj);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else if (result.getResultCode() == OBJECT_DELETE) {
                        games.clear();
                        gameListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.VISIBLE);

                        assert result.getData() != null;
                        Game game_obj = (Game) result.getData().getSerializableExtra("game_obj");

                        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                db_ref.child("Games").child(game_obj.getKey()).removeValue();
                                db_ref.child("Game_comments").child(game_obj.getKey()).removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
    );


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = new Auth(this);
        if (auth.getUsername() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        textView = findViewById(R.id.hello_user_main);

        db_ref.child("Users").orderByChild("login").equalTo(auth.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    user = snapshot.child(auth.getUsername()).getValue(User.class);
                    assert user != null;
                    auth.setUser(user.getLogin(), user);
                    textView.setText("Hello, " + auth.getUser().getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView = findViewById(R.id.game_list);
        progressBar = findViewById(R.id.progressBar_main);

        games = new ArrayList<>();
        gameListAdapter = new GameListAdapter(this, games);
        listView.setAdapter(gameListAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(MainActivity.this, EditActivity.class);

                db_ref.child("Games").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        intent.putExtra("game_obj", snapshot.child(view.getTag().toString()).getValue(Game.class));
                        activityResult.launch(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_LONG).show();
                    }
                });

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(MainActivity.this, CommentActivity.class);

                db_ref.child("Games").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        intent.putExtra("game_key", snapshot.child(view.getTag().toString()).getValue(Game.class).getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        db_ref.child("Games").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                games.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    games.add(data.getValue(Game.class));
                }

                progressBar.setVisibility(View.GONE);
                gameListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database load error",
                        Toast.LENGTH_LONG).show();
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
            activityResult.launch(new Intent(this, AddItemActivity.class));
        } else if (item.getItemId() == R.id.logout_option) {
            auth.setUser(null, null);
            intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}