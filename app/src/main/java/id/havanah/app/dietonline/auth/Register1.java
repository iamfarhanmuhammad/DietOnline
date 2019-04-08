package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 00:06
 * on 01/04/2019.
 * Havanah Team, ID.
 */
public class Register1 extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    ProgressDialog progressDialog;
    private EditText inputName;
    private EditText inputAddress;
    private EditText inputPhone;
    private EditText inputBirthDate;
    private EditText inputBirthMonth;
    private EditText inputBirthYear;
    private RadioGroup inputGenderOption;
    private RadioButton inputGender;
    private CheckBox agreement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view_1);

        inputName = findViewById(R.id.input_name);
        inputAddress = findViewById(R.id.input_address);
        inputPhone = findViewById(R.id.input_phone);
        inputBirthDate = findViewById(R.id.input_birth_date);
        inputBirthMonth = findViewById(R.id.input_birth_month);
        inputBirthYear = findViewById(R.id.input_birth_year);
        inputGenderOption = findViewById(R.id.input_gender);

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
            String address = inputAddress.getText().toString();
            String phone = inputPhone.getText().toString();
            String date = inputBirthDate.getText().toString();
            String month = inputBirthMonth.getText().toString();
            String year = inputBirthYear.getText().toString();
            String birth_date = month + "/" + date + "/" + year;

            int inputGenderId = inputGenderOption.getCheckedRadioButtonId();
            inputGender = findViewById(inputGenderId);
            String gender = inputGender.getText().toString();
            if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !birth_date.isEmpty() && !gender.isEmpty() && agreement.isChecked()) {
                registerUser(username, email, password, city, subdistrict, name, address, phone, birth_date, gender);
            } else if (!agreement.isChecked()) {
                Toast.makeText(getApplicationContext(), "Please check the agreement checkbox!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String username, final String email, final String password, final String city, final String subdistrict,
                              final String name, final String address, final String phone, final String birth_date, final String gender) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebConfig.auth, response -> {
            Log.d(TAG, "Register Response: " + response);
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    Toast.makeText(Register1.this, "Successfully registered, congrats!", Toast.LENGTH_SHORT).show();
                    // Launch login activity
                    Intent intent = new Intent(Register1.this, Login.class);
                    startActivity(intent);
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

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("city", city);
                params.put("subdistrict", subdistrict);
                params.put("name", name);
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
}
