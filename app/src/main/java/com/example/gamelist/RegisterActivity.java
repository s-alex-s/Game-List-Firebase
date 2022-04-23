package com.example.gamelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import classes.Auth;
import classes.User;

public class RegisterActivity extends AppCompatActivity {
    EditText name_txt, login_txt, password_txt;
    FirebaseDatabase database;
    DatabaseReference db_ref;
    ConstraintLayout root_view;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        db_ref = database.getReference().child("Users");
        auth = new Auth(this);

        name_txt = findViewById(R.id.reg_name);
        login_txt = findViewById(R.id.reg_login);
        password_txt = findViewById(R.id.reg_password);

        root_view = findViewById(R.id.reg_root);
    }

    public void clicked_register(View view) {
        close_keyboard();

        String name = name_txt.getText().toString().trim();
        String login = login_txt.getText().toString().trim();
        String password = password_txt.getText().toString().trim();

        boolean is_ok = true;

        if (name.isEmpty()) {
            name_txt.setError("Enter your name");
            is_ok = false;
        }
        if (login.isEmpty()) {
            login_txt.setError("Enter login");
            is_ok = false;
        }
        if (password.isEmpty()) {
            password_txt.setError("Enter password");
            is_ok = false;
        }

        if (is_ok) {
            db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(login).exists()) {
                        Snackbar.make(root_view, "This user already exist", Snackbar.LENGTH_SHORT).show();
                    } else {
                        User user = new User(name, password, login);
                        db_ref.child(login).setValue(user);
                        auth.setUser(login, user);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar.make(root_view, error.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void clicked_cancel(View view) {
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