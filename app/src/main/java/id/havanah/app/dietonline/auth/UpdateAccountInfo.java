package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;
import id.havanah.app.dietonline.Profile;

/**
 * Created by farhan at 23:14
 * on 09/04/2019.
 * Havanah Team, ID.
 */
public class UpdateAccountInfo extends AppCompatActivity {

    private static final String TAG = UpdateAccountInfo.class.getSimpleName();
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog progressDialog;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_account_info_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        UserData userData = new UserData();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        inputUsername = findViewById(R.id.input_username);
        inputUsername.setText(userData.getUsername());
        inputEmail = findViewById(R.id.input_email);
        inputEmail.setText(userData.getEmail());
        inputPassword = findViewById(R.id.input_password);

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> {
            String old_username = userData.getUsername();
            String old_email = userData.getEmail();
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (!old_username.isEmpty() && !old_email.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                updateUser(old_username, old_email, username, email, password);
            } else {
                Toast.makeText(this, "Please entry your details completely!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void updateUser(String old_username, String old_email, String username, String email, String password) {
        String tag_string_req = "req_updating";

        progressDialog.setMessage("Updating ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ApiService.updateAccount, response -> {
            Log.d(TAG, "Processing response : " + response);
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    session.setLogin(true);
                    String uid = jsonObject.getString("uid");

                    JSONObject user = jsonObject.getJSONObject("user");
                    String username1 = user.getString("username");
                    String email1 = user.getString("email");
                    String updated_at = user.getString("updated_at");

                    Toast.makeText(this, "Successfully updated, congrats!", Toast.LENGTH_SHORT).show();

                    db.updateAccountInfo(username1, email1, updated_at);
                    Intent intent = new Intent(UpdateAccountInfo.this, Profile.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "Updating Session Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("tag", "update_account_info");
                params.put("old_username", old_username);
                params.put("old_email", old_email);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
