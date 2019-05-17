package id.havanah.app.dietonline.transaction.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.transaction.OrderSubmit;

/**
 * Created by farhan at 07:51
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class WeightLossPackageView extends AppCompatActivity {

    private static final String TAG = WeightLossPackageView.class.getSimpleName();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_weight_loss_package_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        UserData userData = new UserData();
        EditText inputNotes = findViewById(R.id.input_note);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> createTransaction(userData.getUid(), inputNotes.getText().toString()));

    }

    private void createTransaction(String u, String n) {
        String tag_string_req = "req_transaction";
        progressDialog.setMessage(getResources().getString(R.string.processing_transaction));
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.createTransactionWeightLoss, response -> {
            Log.d(TAG, "Transaction Response: " + response);
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    Toast.makeText(WeightLossPackageView.this, getResources().getString(R.string.successfully_create_transaction), Toast.LENGTH_SHORT).show();
                    JSONArray transactions = jsonObject.getJSONArray("transactions");
                    JSONObject data = transactions.getJSONObject(0);

                    // Launch main activity
                    Intent intent = new Intent(WeightLossPackageView.this, OrderSubmit.class);
                    intent.putExtra("amount", 1);
                    intent.putExtra("price", 1100000);
                    intent.putExtra("invoice", data.getString("invoice"));
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.notice_unpaid), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login_view url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", u);
                params.put("notes", n);
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
