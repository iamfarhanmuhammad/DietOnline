package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import id.havanah.app.dietonline.Home;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 10:56
 * on 31/03/2019.
 * Havanah Team, ID.
 */
public class Register extends AppCompatActivity {

    private AnimationDrawable animationDrawable;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner inputCity;
    private Spinner inputSubdistrict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        CoordinatorLayout layout = findViewById(R.id.layout_register);
        animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.setEnterFadeDuration(100);

        inputUsername = findViewById(R.id.input_username);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputCity = findViewById(R.id.input_city);
        inputSubdistrict = findViewById(R.id.input_subdistrict);

        TextView linkToLogin = findViewById(R.id.btn_toLogin);
        linkToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        });

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        SessionManager session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this, Home.class);
            startActivity(intent);
            finish();
        }

        // Next Form Button Click event
        Button btnNextForm = findViewById(R.id.btn_next_form_register);
        btnNextForm.setOnClickListener(view -> {
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String city = inputCity.getSelectedItem().toString();
            String subdistrict = inputSubdistrict.getSelectedItem().toString();

            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !city.isEmpty() && !subdistrict.isEmpty()) {
                Intent intent = new Intent(Register.this, Register1.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("city", city);
                intent.putExtra("subdistrict", subdistrict);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        });
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
}
