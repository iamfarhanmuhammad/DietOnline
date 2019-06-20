package id.havanah.app.dietonline.transaction.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

import java.util.HashSet;
import java.util.Set;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.util.Utils;

/**
 * Created by farhan at 12:15
 * on 18/06/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageTimesToSend extends AppCompatActivity {

    Utils utils;
    MultiSelectToggleGroup multiDays, multiTimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_specific_package_time_to_send);

        utils = new Utils(this);

        // Init input data
        multiDays = findViewById(R.id.multiSelectToggleGroup_day);
        String[] daysArray = getResources().getStringArray(R.array.days);
        for (String text : daysArray) {
            LabelToggle toggle = new LabelToggle(this);
            toggle.setText(text);
            toggle.setMarkerColor(getResources().getColor(R.color.colorPrimary));
            multiDays.addView(toggle);
        }

        multiTimes = findViewById(R.id.multiSelectToggleGroup_time);
        String[] timesArray = getResources().getStringArray(R.array.times);
        for (String text : timesArray) {
            LabelToggle toggle = new LabelToggle(this);
            toggle.setText(text);
            toggle.setMarkerColor(getResources().getColor(R.color.colorPrimary));
            multiTimes.addView(toggle);
        }

        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {

            Set<Integer> daysCheckedIds = multiDays.getCheckedIds();
            Set<Integer> daysChecked = new HashSet<>(); // Holder for all checked positions
            for (int id : daysCheckedIds) {
                View view = multiDays.findViewById(id);
                int position = multiDays.indexOfChild(view);
                daysChecked.add(position);
            }

            Set<Integer> timesCheckedIds = multiTimes.getCheckedIds();
            Set<Integer> timesChecked = new HashSet<>(); // Holder for all checked positions
            for (int id : timesCheckedIds) {
                View view = multiTimes.findViewById(id);
                int position = multiTimes.indexOfChild(view);
                timesChecked.add(position);
            }

            int[] selectedDays = utils.convertSetIntegerToIntArray(daysChecked, 1);
            int[] selectedTimes = utils.convertSetIntegerToIntArray(timesChecked, 1);

            String product_id = getIntent().getStringExtra("product_id");
            String days = utils.convertIntArrayToString(selectedDays);
            String times = utils.convertIntArrayToString(selectedTimes);

            Intent intent = new Intent(SpecificPackageTimesToSend.this, SpecificPackageActivities.class);
            intent.putExtra("product_id", product_id);
            intent.putExtra("days", days);
            intent.putExtra("times", times);
            startActivity(intent);

        });
    }
}
