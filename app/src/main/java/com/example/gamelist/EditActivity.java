package com.example.gamelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import classes.Game;

public class EditActivity extends AppCompatActivity {
    Intent intent;
    TextView name;
    TextView date;
    TextView genre;
    Bundle arguments;
    Game game_obj;
    public final int OBJECT_EDIT = 4;
    public final int OBJECT_DELETE = 5;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = findViewById(R.id.edit_game_name);
        date = findViewById(R.id.edit_game_date);
        genre = findViewById(R.id.edit_game_genre);

        arguments = getIntent().getExtras();

        game_obj = (Game) arguments.get("game_obj");

        name.setText(game_obj.getName());
        date.setText(game_obj.getRelease_date() + "");
        genre.setText(game_obj.getGenre());
    }

    public void clicked_save(View v) {
        String sname = name.getText().toString();
        String sdate = date.getText().toString();
        String sgenre = genre.getText().toString();
        boolean is_ok = true;

        if (sname.isEmpty()) {
            name.setError("Empty input");
            is_ok = false;
        }
        if (sdate.isEmpty()) {
            date.setError("Empty input");
            is_ok = false;
        } else {
            try {
                Integer.parseInt(sdate);
            } catch (NumberFormatException n) {
                is_ok = false;
                date.setError("Incorrect input");
            }
        }
        if (sgenre.isEmpty()) {
            genre.setError("Empty input");
            is_ok = false;
        }

        if (is_ok) {
            if (!(game_obj.getName().equals(sname) && Integer.toString(game_obj.getRelease_date()).equals(sdate)
                    && game_obj.getGenre().equals(sgenre))) {
                intent = new Intent();

                game_obj.setName(sname);
                game_obj.setRelease_date(Integer.parseInt(sdate));
                game_obj.setGenre(sgenre);

                intent.putExtra("game_obj", game_obj);
                setResult(OBJECT_EDIT, intent);
            }

            finish();
        }
    }

    public void clicked_delete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Delete game?");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    intent = new Intent();
                    intent.putExtra("game_obj", game_obj);
                    setResult(OBJECT_DELETE, intent);
                    finish();
                }
            }
        };

        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("No", listener);

        builder.create().show();
    }

    public void clicked_cancel(View v) {
        finish();
    }
}