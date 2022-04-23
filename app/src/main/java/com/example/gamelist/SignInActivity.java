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

public class SignInActivity extends AppCompatActivity {
    EditText login_txt, password_txt;
    FirebaseDatabase database;
    DatabaseReference db_ref;
    User user;
    Auth auth;
    Intent intent;

    ConstraintLayout root_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database = FirebaseDatabase.getInstance();
        db_ref = database.getReference().child("Users");

        login_txt = findViewById(R.id.sign_in_login);
        password_txt = findViewById(R.id.sign_in_password);

        root_view = findViewById(R.id.sign_in_root);

        auth = new Auth(this);
    }

    public void clicked_signIn(View view) {
        close_keyboard();

        String login = login_txt.getText().toString().trim();
        String password = password_txt.getText().toString().trim();
        boolean is_ok = true;

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
                        user = snapshot.child(login).getValue(User.class);
                        assert user != null;
                        if (user.getPassword().equals(password)) {
                            auth.setUser(login, user);
                            intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(root_view, "Incorrect login or password", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(root_view, "Incorrect login or password", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar.make(root_view, "Database error", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void clicked_register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void close_keyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}