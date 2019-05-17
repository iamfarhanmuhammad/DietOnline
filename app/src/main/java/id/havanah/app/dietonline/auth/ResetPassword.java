package id.havanah.app.dietonline.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.havanah.app.dietonline.R;

/**
 * Created by farhan at 10:02
 * on 04/05/2019.
 * Havanah Team, ID.
 */
public class ResetPassword extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> startActivity(new Intent(this, Login.class)));
    }
}
