package id.havanah.app.dietonline.transaction.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.auth.UpdateMedicalInfo;

/**
 * Created by farhan at 07:49
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_specific_package_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        initPopup();
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> startActivity(new Intent(this, SpecificPackageForward.class)));

    }

    private void initPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.order_spesific_package_view_popup);
        Button btnEnsure = dialog.findViewById(R.id.btn_ensure);
        btnEnsure.setOnClickListener(v -> {
            Intent intent = new Intent(SpecificPackageView.this, UpdateMedicalInfo.class);
            startActivity(intent);
        });
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
