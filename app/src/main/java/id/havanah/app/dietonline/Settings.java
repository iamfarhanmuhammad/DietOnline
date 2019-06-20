package id.havanah.app.dietonline;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 23:32
 * on 03/05/2019.
 * Havanah Team, ID.
 */
public class Settings extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        progressDialog = new ProgressDialog(this);

        CardView accountSettings = findViewById(R.id.toAccountSettings);
        accountSettings.setOnClickListener(v -> {
            initAccountSettingsPopup();
        });
    }

    private void initAccountSettingsPopup() {
        Dialog dialog = new Dialog(Settings.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.settings_account_popup);
        Button btnDeleteAccount = dialog.findViewById(R.id.btn_delete_account);
        btnDeleteAccount.setOnClickListener(v -> deleteUser());
        Button btnLogout = dialog.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());
        dialog.show();
    }

    private void logoutUser() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(Settings.this, SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(Settings.this, mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) Settings.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    private void deleteUser() {
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        UserData userData = new UserData();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.deleteAccount, response -> {
            progressDialog.dismiss();
            logoutUser();
            Toast.makeText(this, getResources().getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(Settings.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userData.getUid());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
