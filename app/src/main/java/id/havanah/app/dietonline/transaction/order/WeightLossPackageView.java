package id.havanah.app.dietonline.transaction.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.havanah.app.dietonline.R;

/**
 * Created by farhan at 07:51
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class WeightLossPackageView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_weight_loss_package_view);

        Spanned policy = Html.fromHtml(getString(R.string.package_weight_loss_description));
        TextView termsOfUse = findViewById(R.id.textView_weightLossPackageDesc);
        termsOfUse.setText(policy);
        termsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView btnForward = findViewById(R.id.btn_forwardMayo);
        btnForward.setOnClickListener(v -> startActivity(new Intent(WeightLossPackageView.this, WeightLossPackageForward.class)));

    }
}
