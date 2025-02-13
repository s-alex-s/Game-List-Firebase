package com.example.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {
    Intent intent;
    TextView name;
    TextView date;
    TextView genre;
    Bundle arguments;
    public final int OBJECT_ADD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        genre = findViewById(R.id.genre);

        arguments = getIntent().getExtras();
    }

    public void clicked(View v) {
        close_keyboard();

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
            intent = new Intent();
            intent.putExtra("name", sname);
            intent.putExtra("date", sdate);
            intent.putExtra("genre", sgenre);
            setResult(OBJECT_ADD, intent);

            finish();
        }
    }

    public void cancel(View v) {
        finish();
    }

    public void close_keyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}