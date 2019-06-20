package id.havanah.app.dietonline.auth;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import id.havanah.app.dietonline.Home;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SessionManager;
import id.havanah.app.dietonline.lib.SmoothCheckBox;

/**
 * Created by farhan at 00:06
 * on 01/04/2019.
 * Havanah Team, ID.
 */
public class Register1 extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    private AnimationDrawable animationDrawable;
    private ProgressDialog progressDialog;
    private EditText inputName;
    private EditText inputNickname;
    private EditText inputAddress;
    private EditText inputPhone;
    private EditText inputBirthDay;
    private DatePicker datePicker;
    private Calendar calendar;
    private int day, month, year;
    private SegmentedButtonGroup inputGender;
    private SmoothCheckBox agreement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view_1);

        CoordinatorLayout layout = findViewById(R.id.layout_register);
        animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.setEnterFadeDuration(100);

        TextView linkToLogin = findViewById(R.id.btn_toLogin);
        linkToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register1.this, Login.class);
            startActivity(intent);
            finish();
        });

        TextView linkToTermsAndCond = findViewById(R.id.btn_termsAndConditions);
        linkToTermsAndCond.setMovementMethod(LinkMovementMethod.getInstance());
        linkToTermsAndCond.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("http://dion.co.id/docs/Term%20and%20Condition%20DION.pdf"));
            startActivity(browserIntent);
        });

        calendar = Calendar.getInstance();

        inputName = findViewById(R.id.input_name);
        inputNickname = findViewById(R.id.input_nickname);
        inputAddress = findViewById(R.id.input_address);
        inputPhone = findViewById(R.id.input_phone);
        inputBirthDay = findViewById(R.id.input_birthday);
        inputBirthDay.setOnClickListener(v -> showDialog(999));
        inputGender = findViewById(R.id.input_gender);
        agreement = findViewById(R.id.checkbox_agreement);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        SessionManager session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register1.this, Home.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(view -> {

            String username = getIntent().getStringExtra("username");
            String email = getIntent().getStringExtra("email");
            String password = getIntent().getStringExtra("password");
            String city = getIntent().getStringExtra("city");
            String subdistrict = getIntent().getStringExtra("subdistrict");
            String name = inputName.getText().toString();
            String nickname = inputNickname.getText().toString();
            String address = inputAddress.getText().toString();
            String phone = inputPhone.getText().toString();
            String birth_date = month + "/" + day + "/" + year;
            String gender = String.valueOf(inputGender.getPosition());

            if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !birth_date.isEmpty() && !gender.isEmpty() && agreement.isChecked()) {
                registerUser(username, email, password, city, subdistrict, name, nickname, address, phone, birth_date, gender);
            } else if (!agreement.isChecked()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.agreement_unchecked), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Pilih Tangal", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999)
            return new DatePickerDialog(this, myDateListener, 2000, 4, 12);
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        showDate(arg1, arg2 + 1, arg3);
        setBirthDay(arg1, arg2 + 1, arg3);
    };

    private void setBirthDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private void showDate(int year, int month, int day) {
        inputBirthDay.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String username, final String email, final String password, final String city, final String subdistrict,
                              final String name, final String nickname, final String address, final String phone, final String birth_date, final String gender) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.register, response -> {
            Log.d(TAG, "Register Response: " + response);
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    Toast.makeText(Register1.this, "Successfully registered, congrats!", Toast.LENGTH_SHORT).show();
                    // Launch login activity
                    startActivity(new Intent(Register1.this, Login.class));
                    finish();
                } else {

                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Log.e(TAG, "Registration Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("city", city);
                params.put("subdistrict", subdistrict);
                params.put("name", name);
                params.put("nickname", nickname);
                params.put("address", address);
                params.put("phone", phone);
                params.put("birth_date", birth_date);
                params.put("gender", gender);

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
