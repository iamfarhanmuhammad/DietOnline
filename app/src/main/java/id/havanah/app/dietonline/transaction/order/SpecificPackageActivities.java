package id.havanah.app.dietonline.transaction.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.ActivitiesAdapter;
import id.havanah.app.dietonline.auth.UpdateMedicalInfo;
import id.havanah.app.dietonline.model.ActivitiesModel;

/**
 * Created by farhan at 23:37
 * on 03/06/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageActivities extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;
    private ArrayList<ActivitiesModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_specific_package_activities);

        recyclerView = findViewById(R.id.recyclerView_specificPackageForward);
        adapter = new ActivitiesAdapter(this, list);
        recyclerView.setAdapter(adapter);
        initItem();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initPopup();
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            if (adapter.getSelected() != null) {
                String activity = String.valueOf((list.indexOf(adapter.getSelected()) + 1));
                Intent intent = new Intent(this, SpecificPackageFoodType.class);
                intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
                intent.putExtra("days", getIntent().getStringExtra("days"));
                intent.putExtra("times", getIntent().getStringExtra("times"));
                intent.putExtra("activity", activity);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No Selection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initItem() {
        list = new ArrayList<>();
        list.add(new ActivitiesModel(getResources().getString(R.string.sedentary), getResources().getString(R.string.sedentary_description)));
        list.add(new ActivitiesModel(getResources().getString(R.string.low_active), getResources().getString(R.string.low_active_description)));
        list.add(new ActivitiesModel(getResources().getString(R.string.active), getResources().getString(R.string.active_description)));
        list.add(new ActivitiesModel(getResources().getString(R.string.very_active), getResources().getString(R.string.very_active_description)));
        adapter.setActivity(list);
    }

    private void initPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.order_spesific_package_validate_medic_popup);
        Button btnEnsure = dialog.findViewById(R.id.btn_ensure);
        btnEnsure.setOnClickListener(v -> {
            Intent intent = new Intent(SpecificPackageActivities.this, UpdateMedicalInfo.class);
            startActivity(intent);
        });
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
