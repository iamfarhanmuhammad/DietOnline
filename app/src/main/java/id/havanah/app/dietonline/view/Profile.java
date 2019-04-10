package id.havanah.app.dietonline.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.auth.Login;
import id.havanah.app.dietonline.auth.UpdateAccountInfo;
import id.havanah.app.dietonline.auth.UpdateMedicalInfo;
import id.havanah.app.dietonline.auth.UpdatePersonalInfo;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.helper.SQLiteHandler;
import id.havanah.app.dietonline.helper.SessionManager;

/**
 * Created by farhan at 19:58
 * on 04/04/2019.
 * Havanah Team, ID.
 */
public class Profile extends AppCompatActivity implements View.OnClickListener {

    private SessionManager sessionManager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        ImageView backButton = findViewById(R.id.home);
        backButton.setOnClickListener(v -> onBackPressed());

        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        // User data
        initUserData();
        // BMI Chart Graph
        initBMIChart();

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void initUserData() {
        ImageView btnEditPersonalInfo = findViewById(R.id.btn_editPersonalInfo);
        ImageView btnEditAccountInfo = findViewById(R.id.btn_editAccountInfo);
        ImageView btnEditMedicalInfo = findViewById(R.id.btn_editMedicalInfo);
        btnEditPersonalInfo.setOnClickListener(this);
        btnEditAccountInfo.setOnClickListener(this);
        btnEditMedicalInfo.setOnClickListener(this);

        UserData userData = new UserData();

        // Header data
        TextView userNameHeader = findViewById(R.id.textView_userNameHeader);
        TextView userWeightHeader = findViewById(R.id.textView_userWeightHeader);
        TextView userHeightHeader = findViewById(R.id.textView_userHeightHeader);
        TextView userGenderHeader = findViewById(R.id.textView_userGenderHeader);
        TextView userAgeHeader = findViewById(R.id.textView_userAgeHeader);
        userNameHeader.setText(userData.getName());
        if (userData.getWeight() == null) {
            userWeightHeader.setText("?? Kg");
            userHeightHeader.setText("?? Cm");
        } else {
            userWeightHeader.setText(String.format("%s Kg", userData.getWeight()));
            userHeightHeader.setText(String.format("%s Cm", userData.getHeight()));
        }
        userGenderHeader.setText(userData.getGender());
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
        userBirthDate.setText(userData.getBirth_date());
        try {
            userAge.setText(String.format("%s %s", userData.getAge(), getResources().getString(R.string.years_old)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userGender.setText(userData.getGender());
        userUsername.setText(userData.getUsername());
        userEmail.setText(userData.getEmail());
        userCreated.setText(userData.getCreated_at());
        userLastUpdated.setText(userData.getUpdated_at());
        if (userData.getWeight() == null) {
            userWeight.setText("?? Kg");
            userHeight.setText("?? Cm");
            userBmi.setText("??");
            userProhibition.setText("??");
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
                new DataPoint(0, 60),
                new DataPoint(1, 57),
                new DataPoint(2, 54),
                new DataPoint(3, 50),
                new DataPoint(4, 45)
        });
        weightGraph.addSeries(series);
    }

    private void logoutUser() {
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(Profile.this, Login.class);
        startActivity(intent);
        finish();
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
