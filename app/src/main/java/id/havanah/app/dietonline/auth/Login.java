package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.havanah.app.dietonline.Home;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.WebConfig;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 23:12
 * on 28/03/2019.
 * Havanah Team, ID.
 */
public class Login extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = Login.class.getSimpleName();
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        TextView linkToRegister = findViewById(R.id.btn_toRegister);
        linkToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
            finish();
        });

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Sqlite
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> {
            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();

            // Check for empty data in the form
            if (username.trim().length() > 0 && password.trim().length() > 0) {
                // login_view user
                checkLogin(username, password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * function to verify login_view details in mysql db
     */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.auth, response -> {
            Log.d(TAG, "Login Response: " + response);
            hideDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login_view session
                    session.setLogin(true);

                    // Now store the user in SQLite
                    String uid = jsonObject.getString("uid");

                    JSONObject user = jsonObject.getJSONObject("user");
                    String username1 = user.getString("username");
                    String email = user.getString("email");
                    String city = user.getString("city");
                    String subdistrict = user.getString("subdistrict");
                    String name = user.getString("name");
                    String nickname = user.getString("nickname");
                    String address = user.getString("address");
                    String phone = user.getString("phone");
                    String birth_date = user.getString("birth_date");
                    String gender = user.getString("gender");
                    String created_at = user.getString("created_at");

                    Toast.makeText(Login.this, "Successfully logged in, congrats!", Toast.LENGTH_SHORT).show();

                    // Inserting row in users table
                    db.addUser(uid, username1, email, city, subdistrict, name, nickname, address, phone, birth_date, gender, created_at);

                    // Launch main activity
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login_view. Get the error message
                    String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
            }

        }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login_view url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
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
