package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.havanah.app.dietonline.Home;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 23:12
 * on 28/03/2019.
 * Havanah Team, ID.
 */
public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();
    private AnimationDrawable animationDrawable;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        CoordinatorLayout layout = findViewById(R.id.layout_login);
        animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.setEnterFadeDuration(100);

        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);

        TextView linkToRegister = findViewById(R.id.btn_toRegister);
        linkToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
            finish();
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
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
        TextView resetPass = findViewById(R.id.btn_toForgotPass);
        resetPass.setOnClickListener(v -> startActivity(new Intent(this, ResetPassword.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    /**
     * function to verify login_view details in mysql db
     */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.login, response -> {
            Log.d(TAG, "Login Response: " + response);
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                // Check for error node in json
                if (!error) {
                    session.setLogin(true);

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
                    String updated_at = user.getString("updated_at");
                    String weight = user.getString("weight");
                    String height = user.getString("height");
                    String prohibition = user.getString("prohibition");

                    Toast.makeText(Login.this, getResources().getString(R.string.successfully_login), Toast.LENGTH_SHORT).show();

                    // Inserting row in users table
                    db.addUser(uid, username1, email, city, subdistrict, name, nickname, address, phone, birth_date, gender, created_at, updated_at);
                    db.updateMedicalInfo(weight, height, prohibition, updated_at);
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
                e.printStackTrace();
            }

        }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login_view url
                Map<String, String> params = new HashMap<>();
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
