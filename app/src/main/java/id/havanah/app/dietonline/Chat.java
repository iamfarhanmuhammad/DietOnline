package id.havanah.app.dietonline;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by farhan at 23:37
 * on 03/05/2019.
 * Havanah Team, ID.
 */
public class Chat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coming_soon);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
