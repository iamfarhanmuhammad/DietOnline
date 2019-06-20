package id.havanah.app.dietonline;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by farhan at 23:41
 * on 03/05/2019.
 * Havanah Team, ID.
 */
public class ManualBook extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_book);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
