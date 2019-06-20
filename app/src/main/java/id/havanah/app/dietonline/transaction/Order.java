package id.havanah.app.dietonline.transaction;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

import id.havanah.app.dietonline.Home;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.PackageAdapter;
import id.havanah.app.dietonline.model.PackageModel;

/**
 * Created by farhan at 07:18
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class Order extends AppCompatActivity {

    private CoordinatorLayout layout;
    private PackageAdapter adapter;
    private List<PackageModel> list;
    private Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_view);

        layout = findViewById(R.id.layout_order);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(Order.this, Home.class));
            finish();
        });

        TextView title = findViewById(R.id.textView_orderTitle);
        TextView subtitle = findViewById(R.id.textView_orderSubtitle);

        list = new ArrayList<>();
        list.add(new PackageModel(getResources().getString(R.string.package_daily), R.drawable.img_daily_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_lunch), R.drawable.img_lunch_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_specific), R.drawable.img_specific_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_weight_loss), R.drawable.img_weight_loss_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.space), R.drawable.img_recommended_package_slide));

        adapter = new PackageAdapter(list, this);
        ViewPager viewPager = findViewById(R.id.viewPager_package);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0, 100, 0);

        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView_package);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setAnimationType(AnimationType.WORM);

        colors = new Integer[]{getResources().getColor(R.color.white),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimary)
        };

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    layout.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                    pageIndicatorView.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.order_now));
                    title.setTextColor(getResources().getColor(R.color.black));
                    subtitle.setText(getResources().getString(R.string.choose_package));
                    subtitle.setTextColor(getResources().getColor(R.color.black));
                } else if (position == list.size() - 1) {
                    pageIndicatorView.setVisibility(View.GONE);
                    title.setText(getResources().getString(R.string.package_recommended));
                    title.setTextColor(getResources().getColor(R.color.white));
                    subtitle.setText(getResources().getString(R.string.package_recommended_desc));
                    subtitle.setTextColor(getResources().getColor(R.color.white));
                } else {
                    layout.setBackgroundColor(colors[colors.length - 1]);
                    pageIndicatorView.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.order_now));
                    title.setTextColor(getResources().getColor(R.color.black));
                    subtitle.setText(getResources().getString(R.string.choose_package));
                    subtitle.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
