package id.havanah.app.dietonline.transaction.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;

import java.util.List;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.util.Utils;

/**
 * Created by farhan at 15:39
 * on 14/06/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageFoodType extends AppCompatActivity {

    String productId;
    String activity;
    String foodType;
    String disease;
    String note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_specific_package_food_type);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        Utils utils = new Utils(this);

        CompoundButtonGroup foodTypes = findViewById(R.id.input_foodType);
        CompoundButtonGroup diseases = findViewById(R.id.input_disease);
        EditText notes = findViewById(R.id.input_note);

        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            List<Integer> foodTypePosition = foodTypes.getCheckedPositions();
            List<Integer> diseasePosition = diseases.getCheckedPositions();
            int[] foodTypeChecked = utils.convertListIntegerToIntArray(foodTypePosition);
            int[] diseaseChecked = utils.convertListIntegerToIntArray(diseasePosition);

            foodType = utils.convertIntArrayToString(foodTypeChecked);
            disease = utils.convertIntArrayToString(diseaseChecked);
            note = notes.getText().toString();

            if (note.isEmpty() || foodType.isEmpty() || disease.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.notice_incompleted_form), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SpecificPackageFoodType.this, SpecificPackageUploadImage.class);
                intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
                intent.putExtra("days", getIntent().getStringExtra("days"));
                intent.putExtra("times", getIntent().getStringExtra("times"));
                intent.putExtra("activity", getIntent().getStringExtra("activity"));
                intent.putExtra("food_type", foodType);
                intent.putExtra("sickness", disease);
                intent.putExtra("notes", note);
                startActivity(intent);
            }

        });

    }
}
