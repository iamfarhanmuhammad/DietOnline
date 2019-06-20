package id.havanah.app.dietonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UpdateAccountInfo;
import id.havanah.app.dietonline.auth.UpdateMedicalInfo;
import id.havanah.app.dietonline.auth.UpdatePersonalInfo;
import id.havanah.app.dietonline.auth.UserData;

/**
 * Created by farhan at 19:58
 * on 04/04/2019.
 * Havanah Team, ID.
 */
public class Profile extends AppCompatActivity implements View.OnClickListener {

    private UserData userData;
    private float paidAmount, doneAmount;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        ImageView backButton = findViewById(R.id.home);
        backButton.setOnClickListener(v -> onBackPressed());

        userData = new UserData();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Displaying available package using Progress bar
        RoundCornerProgressBar cornerProgressBar = findViewById(R.id.cornerProgressActivePackage);
        TextView remainingPackage = findViewById(R.id.textView_remainingPackage);
        progressDialog.setMessage(getResources().getString(R.string.loading_profile));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.fetchTransactionAmount, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    paidAmount = Float.parseFloat(jsonObject.getString("paid"));
                    doneAmount = Float.parseFloat(jsonObject.getString("done"));
                    float totalAmount = paidAmount + doneAmount;
                    float percentage = paidAmount / totalAmount * 100f;
                    cornerProgressBar.setProgress(percentage);
                    remainingPackage.setText(String.format(Locale.US, "%.0f " + getResources().getString(R.string.remaining), paidAmount));
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

        initUserData();
        initBMIChart();
    }

    private void initUserData() {
        CircleImageView userAvatar = findViewById(R.id.profile_image);
        ImageView btnEditPersonalInfo = findViewById(R.id.btn_editPersonalInfo);
        ImageView btnEditAccountInfo = findViewById(R.id.btn_editAccountInfo);
        ImageView btnEditMedicalInfo = findViewById(R.id.btn_editMedicalInfo);
        btnEditPersonalInfo.setOnClickListener(this);
        btnEditAccountInfo.setOnClickListener(this);
        btnEditMedicalInfo.setOnClickListener(this);

        if (userData.getGender().equalsIgnoreCase("0")) {
            userAvatar.setImageResource(R.drawable.ic_person_male_96px);
        } else userAvatar.setImageResource(R.drawable.ic_person_female_96px);
        // Header data
        CardView cardBmi = findViewById(R.id.cardView_bmiIndicator);
        TextView tvBmi = findViewById(R.id.textView_bmiIndicator);
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

        TextView userNameHeader = findViewById(R.id.textView_userNameHeader);
        TextView userWeightHeader = findViewById(R.id.textView_userWeightHeader);
        TextView userHeightHeader = findViewById(R.id.textView_userHeightHeader);
        TextView userGenderHeader = findViewById(R.id.textView_userGenderHeader);
        TextView userAgeHeader = findViewById(R.id.textView_userAgeHeader);
        userNameHeader.setText(userData.getName());
        if (userData.getWeight() == null || userData.getWeight().equals("0")) {
            userWeightHeader.setText("?? Kg");
            userHeightHeader.setText("?? Cm");
        } else {
            userWeightHeader.setText(String.format("%s Kg", userData.getWeight()));
            userHeightHeader.setText(String.format("%s Cm", userData.getHeight()));
        }
        if (userData.getGender().equalsIgnoreCase("0")) {
            userGenderHeader.setText(getResources().getString(R.string.male));
        } else {
            userGenderHeader.setText(getResources().getString(R.string.female));
        }
        try {
            userAgeHeader.setText(String.format("%s %s", userData.getAge(), getResources().getString(R.string.years_old)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // User data
        TextView userName = findViewById(R.id.textView_userName);
        TextView userNickname = findViewById(R.id.textView_userNickname);
        TextView userCity = findViewById(R.id.textView_userCity);
        TextView userSubdistrict = findViewById(R.id.textView_userSubdistrict);
        TextView userAddress = findViewById(R.id.textView_userAddress);
        TextView userPhone = findViewById(R.id.textView_userPhone);
        TextView userBirthDate = findViewById(R.id.textView_userBirthDate);
        TextView userGender = findViewById(R.id.textView_userGender);
        TextView userAge = findViewById(R.id.textView_userAge);
        TextView userUsername = findViewById(R.id.textView_userUsername);
        TextView userEmail = findViewById(R.id.textView_userEmail);
        TextView userCreated = findViewById(R.id.textView_userCreatedAt);
        TextView userLastUpdated = findViewById(R.id.textView_userUpdatedAt);
        TextView userWeight = findViewById(R.id.textView_userWeight);
        TextView userHeight = findViewById(R.id.textView_userHeight);
        TextView userBmi = findViewById(R.id.textView_userBmi);
        TextView userProhibition = findViewById(R.id.textView_userProhibition);

        userName.setText(userData.getName());
        userNickname.setText(userData.getNickname());
        userCity.setText(userData.getCity());
        userSubdistrict.setText(userData.getSubdistrict());
        userAddress.setText(userData.getAddress());
        userPhone.setText(userData.getPhone());
        try {
            String day = userData.getDate_birth();
            String month = userData.getMonth_birth();
            switch (month) {
                case "1":
                    month = getResources().getString(R.string.january);
                    break;
                case "2":
                    month = getResources().getString(R.string.february);
                    break;
                case "3":
                    month = getResources().getString(R.string.march);
                    break;
                case "4":
                    month = getResources().getString(R.string.april);
                    break;
                case "5":
                    month = getResources().getString(R.string.may);
                    break;
                case "6":
                    month = getResources().getString(R.string.june);
                    break;
                case "7":
                    month = getResources().getString(R.string.july);
                    break;
                case "8":
                    month = getResources().getString(R.string.august);
                    break;
                case "9":
                    month = getResources().getString(R.string.september);
                    break;
                case "10":
                    month = getResources().getString(R.string.october);
                    break;
                case "11":
                    month = getResources().getString(R.string.november);
                    break;
                case "12":
                    month = getResources().getString(R.string.december);
                    break;
            }
            String year = userData.getYear_birth();
            userBirthDate.setText(String.format("%s %s %s", day, month, year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            userAge.setText(String.format("%s %s", userData.getAge(), getResources().getString(R.string.years_old)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (userData.getGender().equalsIgnoreCase("0")) {
            userGender.setText(getResources().getString(R.string.male));
        } else {
            userGender.setText(getResources().getString(R.string.female));
        }
        userUsername.setText(userData.getUsername());
        userEmail.setText(userData.getEmail());
        userCreated.setText(userData.getCreated_at());
        userLastUpdated.setText(userData.getUpdated_at());
        if (userData.getWeight() == null) {
            userWeight.setText("?? Kg");
            userHeight.setText("?? Cm");
            userBmi.setText(getResources().getString(R.string.undefined));
            userProhibition.setText("-");
        } else {
            userWeight.setText(String.format("%s Kg", userData.getWeight()));
            userHeight.setText(String.format("%s Cm", userData.getHeight()));
            userBmi.setText(userData.getBmi());
            userProhibition.setText(userData.getProhibition());
        }
    }

    private void initBMIChart() {
        GraphView weightGraph = findViewById(R.id.graph_weight);
        weightGraph.setTitle(getResources().getString(R.string.weight));
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 57),
                new DataPoint(2, 54),
                new DataPoint(3, 50),
                new DataPoint(4, 50)
        });
        series.setTitle(getResources().getString(R.string.weight));
        series.setColor(getResources().getColor(R.color.colorPrimary));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        weightGraph.addSeries(series);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_editPersonalInfo:
                Toast.makeText(this, getResources().getString(R.string.edit_personal_info), Toast.LENGTH_SHORT).show();
                intent = new Intent(Profile.this, UpdatePersonalInfo.class);
                startActivity(intent);
                break;
            case R.id.btn_editAccountInfo:
                Toast.makeText(this, getResources().getString(R.string.edit_account_info), Toast.LENGTH_SHORT).show();
                intent = new Intent(Profile.this, UpdateAccountInfo.class);
                startActivity(intent);
                break;
            case R.id.btn_editMedicalInfo:
                Toast.makeText(this, getResources().getString(R.string.edit_medical_info), Toast.LENGTH_SHORT).show();
                intent = new Intent(Profile.this, UpdateMedicalInfo.class);
                startActivity(intent);
                break;
        }
    }
}
