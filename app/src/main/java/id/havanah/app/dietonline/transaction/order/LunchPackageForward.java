package id.havanah.app.dietonline.transaction.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.transaction.OrderSubmit;
import id.havanah.app.dietonline.util.Utils;

/**
 * Created by farhan at 00:54
 * on 12/04/2019.
 * Havanah Team, ID.
 */
public class LunchPackageForward extends AppCompatActivity {

    private SQLiteHandler db;
    private UserData userData;
    private Utils utils;
    private ProgressDialog progressDialog;
    //    private MultiSelectToggleGroup multiDays;
    private EditText inputAmount, inputNote;
    private CheckBox agreement;
    private String userId, productId, productName, orderAmount, orderNote;
    private int productImage, productPrice;
    private int[] selectedDays;

    private static final String TAG = LunchPackageForward.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_lunch_package_forward);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userData = new UserData();
        db = new SQLiteHandler(this);
        utils = new Utils(this);
        ImageView image = findViewById(R.id.lunchPackage_image);
        TextView name = findViewById(R.id.lunchPackage_name);
        TextView price = findViewById(R.id.lunchPackage_price);
        inputAmount = findViewById(R.id.input_amount);
        inputNote = findViewById(R.id.input_note);
        agreement = findViewById(R.id.checkbox_agreement);

        userId = userData.getUid();
        productImage = getIntent().getIntExtra("image", -1);
        productId = getIntent().getStringExtra("product_id");
        productName = getIntent().getStringExtra("name");
        productPrice = getIntent().getIntExtra("price", -1);
        image.setImageResource(productImage);
        name.setText(productName);
        price.setText(String.format(Locale.US, "Rp %d", productPrice));

        MultiSelectToggleGroup multiDays = findViewById(R.id.multiSelectToggleGroup_day);
        String[] daysArray = getResources().getStringArray(R.array.days);
        for (String text : daysArray) {
            LabelToggle toggle = new LabelToggle(this);
            toggle.setText(text);
            toggle.setMarkerColor(getResources().getColor(R.color.colorPrimary));
            multiDays.addView(toggle);
        }

        Button btnOrder = findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(v -> {
            orderAmount = inputAmount.getText().toString();
            orderNote = inputNote.getText().toString();

            Set<Integer> daysCheckedIds = multiDays.getCheckedIds();
            Set<Integer> daysChecked = new HashSet<>(); // Holder for all checked positions
            for (int id : daysCheckedIds) {
                View view = multiDays.findViewById(id);
                int position = multiDays.indexOfChild(view);
                daysChecked.add(position);
            }
            selectedDays = utils.convertSetIntegerToIntArray(daysChecked, 1);
            String days = utils.convertIntArrayToString(selectedDays);
            String times = "2";

            if (agreement.isChecked()) {
                if (orderAmount.isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.notice_minimum_amount), Toast.LENGTH_SHORT).show();
                } else if (daysChecked.isEmpty() || times.isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.notice_select_days_and_times), Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(orderAmount) < 5) {
                        Toast.makeText(this, getResources().getString(R.string.notice_minimum_amount), Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(orderAmount) > 100) {
                        Toast.makeText(this, getResources().getString(R.string.notice_maximum_amount), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.processing_transaction), Toast.LENGTH_SHORT).show();
                        createTransaction(userId, productId, days, times, orderAmount, orderNote);
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.agreement_unchecked), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void createTransaction(String u, String p, String d, String t, String a, String n) {
        String tag_string_req = "req_transaction";

        progressDialog.setMessage(getResources().getString(R.string.processing_transaction));
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.createTransaction, response -> {
            Log.d(TAG, "Transaction Response: " + response);
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    Toast.makeText(LunchPackageForward.this, getResources().getString(R.string.successfully_create_transaction), Toast.LENGTH_SHORT).show();
                    JSONObject product = jsonObject.getJSONObject("product");

                    JSONArray transactions = jsonObject.getJSONArray("transactions");
                    JSONObject transaction = transactions.getJSONObject(0);
                    // Launch main activity
                    Intent intent = new Intent(LunchPackageForward.this, OrderSubmit.class);
                    intent.putExtra("amount", a);
                    intent.putExtra("price", product.getString("price"));
                    intent.putExtra("invoice", transaction.getString("invoice"));
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login_view. Get the error message
                    String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.notice_unpaid), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
            }

        }, error -> {
            Log.e(TAG, "Process Error: " + error.getMessage());
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
                params.put("product_id", p);
                params.put("days", d);
                params.put("times", t);
                params.put("amount", a);
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
