package id.havanah.app.dietonline.transaction.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.havanah.app.dietonline.ComingSoon;
import id.havanah.app.dietonline.R;

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

        ImageView btnForward = findViewById(R.id.btn_forwardSpecific);
        btnForward.setOnClickListener(v -> startActivity(new Intent(SpecificPackageView.this, SpecificPackageMenus.class)));

    }
}
