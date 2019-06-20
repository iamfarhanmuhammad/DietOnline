package id.havanah.app.dietonline.transaction.order;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.SpecificPackageAdapter;
import id.havanah.app.dietonline.model.SpecificPackageModel;

/**
 * Created by farhan at 00:29
 * on 14/04/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageMenus extends AppCompatActivity {

    ViewPager viewPager;
    SpecificPackageAdapter adapter;
    List<SpecificPackageModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_specific_package_menus);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        list = new ArrayList<>();
        list.add(new SpecificPackageModel("SP001", "Paket Silver", 1100000, R.drawable.img_daily_package_family_2, null,
                "18 Porsi", "Free Konsultasi Gizi", "Dietician Visit 1x", "Perhitungan dan Penyusunan menu/porsi",
                "---", "Kemasan Premium Bento Food Grade", "3x Pengiriman per Hari", "Fresh from the Kitchen", "Free Souvenir"));
        list.add(new SpecificPackageModel("SP002", "Paket Gold", 1700000, R.drawable.img_daily_package_family_2, null,
                "30 Porsi", "Free Konsultasi Gizi", "Dietician Visit 1x", "Perhitungan dan Penyusunan menu/porsi",
                "Voucher labolatorium klinik Rp 200.000", "Kemasan Premium Bento Food Grade", "3x Pengiriman per Hari", "Fresh from the Kitchen", "Free Souvenir"));
        list.add(new SpecificPackageModel("SP003", "Paket Platinum", 4100000, R.drawable.img_daily_package_family_2, null,
                "90 Porsi", "Free Konsultasi Gizi", "Dietician Visit 2x", "Perhitungan dan Penyusunan menu/porsi",
                "Voucher labolatorium klinik Rp 400.000", "Kemasan Premium Bento Food Grade", "3x Pengiriman per Hari", "Fresh from the Kitchen", "Free Souvenir"));

        adapter = new SpecificPackageAdapter(list, this);
        viewPager = findViewById(R.id.viewPager_specificPackage);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0, 100, 0);
    }
}
