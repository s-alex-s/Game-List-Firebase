package classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {
    private Context context;
    private static String username = null;
    private static User user = null;

    public Auth(Context context) {
        this.context = context;
    }

    public String getUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        return username;
    }

    public void setUser(String username, User user_set) {
        user = user_set;
        Auth.username = username;
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public User getUser() {
        return user;
    }
}
