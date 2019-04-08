package id.havanah.app.dietonline.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by farhan at 08:50
 * on 31/03/2019.
 * Havanah Team, ID.
 */
public class SessionManager {
    private static final String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    // Shared preferences file name
    private static final String PREF_NAME = "userLogin";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    //deklarasi penggunaan session
    public SessionManager(Context context) {
        this.mContext = context;
        // shared pref mode
        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    //sebagai penanda - digunakan saat user akan login
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User Login Session Modified");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}