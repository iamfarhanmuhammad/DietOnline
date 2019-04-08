package id.havanah.app.dietonline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import de.hdodenhof.circleimageview.CircleImageView;
import id.havanah.app.dietonline.auth.Login;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;
import id.havanah.app.dietonline.view.OrderView;
import id.havanah.app.dietonline.view.Profile;

/**
 * Created by farhan at 23:16
 * on 28/03/2019.
 * Havanah Team, ID.
 */
public class Home extends AppCompatActivity {

    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private TextView greeting;
    private SessionManager sessionManager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        // Header
        toolbar = findViewById(R.id.toolbar_home);
        scrollView = findViewById(R.id.scrollView_home);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            int scrollContentHeight = scrollView.getChildAt(0).getHeight();

            int screenHeight = getScreenHeight();
            int statusBarHeight = getStatusBarHeight();
            int color = getResources().getColor(android.R.color.white);
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = (color >> 0) & 0xFF;

            double percent = ((((float) scrollY) / ((float) (scrollContentHeight - screenHeight + statusBarHeight))));
            if (percent >= 0 && percent <= 1)
                toolbar.setBackgroundColor(Color.argb((int) (255.0 * percent), r, g, b));
        });

        RoundCornerProgressBar cornerProgressBar = findViewById(R.id.cornerProgressActivePackage);
        cornerProgressBar.setProgress(40f);
        greeting = findViewById(R.id.textView_homeGreeting);
        getGreeting();

        CircleImageView user_picture = findViewById(R.id.profile_image);
        user_picture.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        });

        TextView user_nickname = findViewById(R.id.textView_nickname);
        // Checking user login status
        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        String nickname = user.get("nickname");
        user_nickname.setText(nickname);

        CardView toOrder = findViewById(R.id.cardView_toOrder);
        toOrder.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, OrderView.class);
            startActivity(intent);
        });
    }

    private void logoutUser() {
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }

    public int getScreenHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public int getStatusBarHeight() {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private void getGreeting() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.US);
        int currentTime = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

        if (currentTime < 12) {
            greeting.setText(getResources().getString(R.string.greeting_morning));
        } else if (currentTime < 18) {
            greeting.setText(getResources().getString(R.string.greeting_afternoon));
        } else if (currentTime < 24) {
            greeting.setText(getResources().getString(R.string.greeting_evening));
        }
    }
}
