package id.havanah.app.dietonline.transaction.order;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.LunchPackageAdapter;
import id.havanah.app.dietonline.model.ProductModel;

/**
 * Created by farhan at 07:46
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class LunchPackageView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_lunch_package_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerView_lunchPackage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LunchPackageAdapter adapter = new LunchPackageAdapter(this);
        recyclerView.setAdapter(adapter);

        //fill with empty objects
        final List<ProductModel> list = new ArrayList<>();
        list.add(new ProductModel("SL001", "Paket Puas Aja", 16000, R.drawable.img_daily_package_personal, "Terdiri dari nasi, 1 porsi lauk hewani, lauk nabati, sayur, kerupuk & sambal"));
        list.add(new ProductModel("SL002", "Paket Puas Banget", 18000, R.drawable.img_daily_package_family_3, "Terdiri dari nasi, 2 porsi lauk hewani, lauk nabati, sayur, kerupuk & sambal"));
        adapter.setItems(list);
    }
}
