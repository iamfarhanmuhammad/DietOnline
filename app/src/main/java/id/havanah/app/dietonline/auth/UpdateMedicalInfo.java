package id.havanah.app.dietonline.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by farhan at 06:40
 * on 10/04/2019.
 * Havanah Team, ID.
 */
public class UpdateMedicalInfo extends AppCompatActivity {

    private static final String TAG = UpdateMedicalInfo.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog progressDialog;
    private EditText inputWeight;
    private EditText inputHeight;
    private EditText inputProhibition;
    private EditText inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_medical_info_view);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        UserData userData = new UserData();

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        inputWeight = findViewById(R.id.input_weight);
        inputWeight.setText(userData.getWeight());
        inputHeight = findViewById(R.id.input_height);
        inputHeight.setText(userData.getHeight());
        inputProhibition = findViewById(R.id.input_prohibition);
        inputProhibition.setText(userData.getProhibition());
        inputPassword = findViewById(R.id.input_password);

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> {
            String username = userData.getUsername();
            String weight = inputWeight.getText().toString();
            String height = inputHeight.getText().toString();
            String prohibition = inputProhibition.getText().toString();
            String password = inputPassword.getText().toString();

            if (!weight.isEmpty() && !height.isEmpty() && !prohibition.isEmpty() && !password.isEmpty()) {
                updateUser(username, weight, height, prohibition, password);
            } else Toast.makeText(this, "Please entry data completely", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateUser(String username, String weight, String height, String prohibition, String password) {
        String tag_string_req = "req_updating";
        progressDialog.setMessage("Updating data...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebConfig.auth, response -> {
            Log.d(TAG, "Processing request: " + response);
            hideDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    session.setLogin(true);
                    String uid = jsonObject.getString("uid");
                    JSONObject user = jsonObject.getJSONObject("user");
                    String weight1 = user.getString("weight");
                    String height1 = user.getString("height");
                    String prohibition1 = user.getString("prohibition");
                    String updated_at = user.getString("updated_at");

                    Toast.makeText(this, "Successfully updated, congrats!", Toast.LENGTH_SHORT).show();
                    db.updateMedicalInfo(weight1, height1, prohibition1, updated_at);

                    Intent intent = new Intent(UpdateMedicalInfo.this, Profile.class);
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
                HashMap<String, String> params = new HashMap<>();
                params.put("tag", "update_medical_info");
                params.put("username", username);
                params.put("weight", weight);
                params.put("height", height);
                params.put("prohibition", prohibition);
                params.put("password", password);
                return params;
            }
        };
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
