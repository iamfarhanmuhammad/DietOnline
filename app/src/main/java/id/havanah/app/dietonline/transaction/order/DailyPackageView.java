package id.havanah.app.dietonline.transaction.order;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.DailyPackageAdapter;
import id.havanah.app.dietonline.model.ProductModel;

/**
 * Created by farhan at 07:43
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class DailyPackageView extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_daily_package_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerView_dailyPackage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DailyPackageAdapter adapter = new DailyPackageAdapter(this);
        recyclerView.setAdapter(adapter);

        //fill with empty objects
        final List<ProductModel> list = new ArrayList<>();
        list.add(new ProductModel("DP001", "Personal", 22000, R.drawable.img_daily_package_personal, getResources().getString(R.string.package_daily_personal_content)));
        list.add(new ProductModel("DP002", "Family-2", 34000, R.drawable.img_daily_package_family_2, getResources().getString(R.string.package_daily_family2_content)));
        list.add(new ProductModel("DP003", "Family-3", 46000, R.drawable.img_daily_package_family_3, getResources().getString(R.string.package_daily_family3_content)));
        adapter.setItems(list);
    }
}
