package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.WebConfig;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;
import id.havanah.app.dietonline.view.Profile;

/**
 * Created by farhan at 13:32
 * on 08/04/2019.
 * Havanah Team, ID.
 */
public class UpdatePersonalInfo extends AppCompatActivity {

    private static final String TAG = UpdatePersonalInfo.class.getSimpleName();
    SessionManager session;
    SQLiteHandler db;
    ProgressDialog progressDialog;
    private EditText inputName;
    private EditText inputNickname;
    private Spinner inputCity;
    private Spinner inputSubdistrict;
    private EditText inputAddress;
    private EditText inputPhone;
    private EditText inputBirthDate;
    private EditText inputBirthMonth;
    private EditText inputBirthYear;
    private RadioGroup inputGenderOption;
    private RadioButton inputGender;
    private EditText inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_personal_info_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        UserData userData = new UserData();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        inputPassword = findViewById(R.id.input_password);
        inputName = findViewById(R.id.input_name);
        inputName.setText(userData.getName());
        inputNickname = findViewById(R.id.input_nickname);
        inputNickname.setText(userData.getNickname());
        inputCity = findViewById(R.id.input_city);
        inputSubdistrict = findViewById(R.id.input_subdistrict);
        inputAddress = findViewById(R.id.input_address);
        inputAddress.setText(userData.getAddress());
        inputPhone = findViewById(R.id.input_phone);
        inputPhone.setText(userData.getPhone());
        inputBirthDate = findViewById(R.id.input_birth_date);
        inputBirthMonth = findViewById(R.id.input_birth_month);
        inputBirthYear = findViewById(R.id.input_birth_year);
        try {
            inputBirthDate.setText(userData.getDate_birth());
            inputBirthMonth.setText(userData.getMonth_birth());
            inputBirthYear.setText(userData.getYear_birth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inputGenderOption = findViewById(R.id.input_gender);

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> {
            String username = userData.getUsername();
            String password = inputPassword.getText().toString();
            String name = inputName.getText().toString();
            String nickname = inputNickname.getText().toString();
            String city = inputCity.getSelectedItem().toString();
            String subdistrict = inputSubdistrict.getSelectedItem().toString();
            String address = inputAddress.getText().toString();
            String phone = inputPhone.getText().toString();
            String date = inputBirthDate.getText().toString();
            String month = inputBirthMonth.getText().toString();
            String year = inputBirthYear.getText().toString();
            String birth_date = month + "/" + date + "/" + year;

            int inputGenderId = inputGenderOption.getCheckedRadioButtonId();
            inputGender = findViewById(inputGenderId);
            String gender = inputGender.getText().toString();
            if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !birth_date.isEmpty() && !gender.isEmpty()) {
                updateUser(username, password, name, nickname, city, subdistrict, address, phone, birth_date, gender);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void updateUser(String username, String password, String name, String nickname, String city, String subdistrict, String address, String phone, String birth_date, String gender) {
        String tag_string_req = "req_updating";

        progressDialog.setMessage("Updating ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebConfig.auth, response -> {
            Log.d(TAG, "Update Response: " + response);
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    session.setLogin(true);

                    // Now store the user in SQLite
                    String uid = jsonObject.getString("uid");

                    JSONObject user = jsonObject.getJSONObject("user");
                    String name1 = user.getString("name");
                    String nickname1 = user.getString("nickname");
                    String city1 = user.getString("city");
                    String subdistrict1 = user.getString("subdistrict");
                    String address1 = user.getString("address");
                    String phone1 = user.getString("phone");
                    String birth_date1 = user.getString("birth_date");
                    String gender1 = user.getString("gender");
                    String updated_at = user.getString("updated_at");

                    Toast.makeText(UpdatePersonalInfo.this, "Successfully updated, congrats!", Toast.LENGTH_SHORT).show();

                    db.updatePersonalInfo(name1, nickname1, city1, subdistrict1, address1, phone1, birth_date1, gender1, updated_at);

                    // Launch profile activity
                    Intent intent = new Intent(UpdatePersonalInfo.this, Profile.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "Updating Session Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("tag", "update_personal_info");
                params.put("username", username);
                params.put("password", password);
                params.put("name", name);
                params.put("nickname", nickname);
                params.put("city", city);
                params.put("subdistrict", subdistrict);
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
