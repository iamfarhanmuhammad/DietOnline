package id.havanah.app.dietonline;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.Login;
import id.havanah.app.dietonline.auth.UpdateMedicalInfo;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;
import id.havanah.app.dietonline.transaction.Order;
import id.havanah.app.dietonline.transaction.OrderStatus;
import id.havanah.app.dietonline.util.Utils;

/**
 * Created by farhan at 23:16
 * on 28/03/2019.
 * Havanah Team, ID.
 */
public class Home extends AppCompatActivity {

    private CardView toOrder, toOrderStatus, toManualBook, toPartner, toDeveloper;
    private SessionManager sessionManager;
    private SQLiteHandler db;
    private UserData userData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Utils utils = new Utils(this);
        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        userData = new UserData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Checking user login status
        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }

        initPopUpMedicalData();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.layout_home);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            startActivity(getIntent());
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        // Header
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setTitle(null);


        CardView toProfile = findViewById(R.id.cardView_toProfile);
        toProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        });

        getGreeting();
        NestedScrollView scrollView = findViewById(R.id.scrollView_home);
        scrollView.setSmoothScrollingEnabled(true);
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
//            int scrollY = scrollView.getScrollY();
//            int scrollContentHeight = scrollView.getChildAt(0).getHeight();
//            int screenHeight = utils.getScreenHeight();
//            int statusBarHeight = utils.getStatusBarHeight();
//            int color = getResources().getColor(android.R.color.white);
//            int r = (color >> 16) & 0xFF;
//            int g = (color >> 8) & 0xFF;
//            int b = (color) & 0xFF;
//
//            double percent = ((((float) scrollY) / ((float) (scrollContentHeight - screenHeight + statusBarHeight))));
//            if (percent >= 0 && percent <= 1)
//                toolbar.setBackgroundColor(Color.argb((int) (255.0 * percent), r, g, b));
//        });

        // Displaying available package using Progress bar
        RoundCornerProgressBar cornerProgressBar = findViewById(R.id.cornerProgressActivePackage);
        progressDialog.setMessage(getResources().getString(R.string.loading_profile));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.fetchTransactionAmount, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    float paidAmount = Float.parseFloat(jsonObject.getString("paid"));
                    float doneAmount = Float.parseFloat(jsonObject.getString("done"));
                    float totalAmount = paidAmount + doneAmount;
                    float percentage = paidAmount / totalAmount * 100f;
                    cornerProgressBar.setProgress(percentage);
                } else {
                    cornerProgressBar.setProgress(0f);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", userData.getUid());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

        CircleImageView userAvatar = findViewById(R.id.profile_image);
        if (userData.getGender().equalsIgnoreCase("0")) {
            userAvatar.setImageResource(R.drawable.ic_person_male_96px);
        } else userAvatar.setImageResource(R.drawable.ic_person_female_96px);
        userAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        });
        TextView userNickname = findViewById(R.id.textView_userNickname);
        userNickname.setText(userData.getNickname());

        CardView cardBmi = findViewById(R.id.cardView_bmiIndicatorHome);
        TextView tvBmi = findViewById(R.id.textView_bmiIndicatorHome);
        double bmi = Double.parseDouble(userData.getBmi());
        if (bmi < 18.4) {
            cardBmi.setCardBackgroundColor(getResources().getColor(R.color.underweight));
            tvBmi.setText(getResources().getString(R.string.underweight));
        } else if (bmi < 25.0) {
            cardBmi.setCardBackgroundColor(getResources().getColor(R.color.ideal));
            tvBmi.setText(getResources().getString(R.string.ideal));
        } else if (bmi < 27.0) {
            cardBmi.setCardBackgroundColor(getResources().getColor(R.color.overweight));
            tvBmi.setText(getResources().getString(R.string.overweight));
        } else if (bmi >= 27.0) {
            cardBmi.setCardBackgroundColor(getResources().getColor(R.color.obese));
            tvBmi.setText(getResources().getString(R.string.obese));
        } else  {
            cardBmi.setCardBackgroundColor(getResources().getColor(R.color.gray));
            tvBmi.setText(getResources().getString(R.string.undefined));
        }

        toOrder = findViewById(R.id.cardView_toMakeAPlan);
        toOrder.setOnClickListener(v -> startActivity(new Intent(Home.this, Order.class)));
        toOrderStatus = findViewById(R.id.cardView_toOrderStatus);
        toOrderStatus.setOnClickListener(v -> startActivity(new Intent(Home.this, OrderStatus.class)));
        toManualBook = findViewById(R.id.cardView_toManualBook);
        toManualBook.setOnClickListener(v -> startActivity(new Intent(Home.this, ManualBook.class)));
        toPartner = findViewById(R.id.cardView_toAboutPartner);
        toPartner.setOnClickListener(v -> startActivity(new Intent(Home.this, AboutPartner.class)));
        toDeveloper = findViewById(R.id.cardView_toAboutDeveloper);
        toDeveloper.setOnClickListener(v -> startActivity(new Intent(Home.this, AboutDeveloper.class)));
    }

    private void initPopUpMedicalData() {
        if (userData.getWeight() == null || userData.getHeight() == null || userData.getWeight().equals("0") || userData.getHeight().equals("0")) {
            displayPopUpMedicalData();
        }
    }

    private void displayPopUpMedicalData() {
        Dialog dialog = new Dialog(Home.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.notice_medical_data_popup);
        Button btnToUpdateMedic = dialog.findViewById(R.id.btn_toUpdateMedic);
        btnToUpdateMedic.setOnClickListener(v -> toUpdateMedic());
        Button btnLater = dialog.findViewById(R.id.btn_later);
        btnLater.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void toUpdateMedic() {
        startActivity(new Intent(Home.this, UpdateMedicalInfo.class));
    }

    private void logoutUser() {
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void getGreeting() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.US);
        int currentTime = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));
        ImageView imageHeader = findViewById(R.id.image_headerCurved);
        TextView greeting = findViewById(R.id.textView_homeGreeting);

        if (currentTime < 12) {
            imageHeader.setBackgroundResource(R.drawable.img_header_morning);
            greeting.setText(getResources().getString(R.string.greeting_morning));
        } else if (currentTime < 17) {
            imageHeader.setBackgroundResource(R.drawable.img_header_afternoon);
            greeting.setText(getResources().getString(R.string.greeting_afternoon));
        } else if (currentTime < 21) {
            imageHeader.setBackgroundResource(R.drawable.img_header_evening);
            greeting.setText(getResources().getString(R.string.greeting_evening));
        } else {
            imageHeader.setBackgroundResource(R.drawable.img_header_night);
            greeting.setText(getResources().getString(R.string.greeting_night));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this, Settings.class));
                break;
            case R.id.chat:
                Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this, Chat.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
