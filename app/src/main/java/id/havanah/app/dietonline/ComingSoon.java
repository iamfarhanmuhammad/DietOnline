package id.havanah.app.dietonline;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by farhan at 19:56
 * on 09/06/2019.
 * Havanah Team, ID.
 */
public class ComingSoon extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coming_soon);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
