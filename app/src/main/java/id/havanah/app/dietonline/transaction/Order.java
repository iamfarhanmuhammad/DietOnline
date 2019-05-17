package id.havanah.app.dietonline.transaction;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.PackageAdapter;
import id.havanah.app.dietonline.model.PackageModel;

/**
 * Created by farhan at 07:18
 * on 06/04/2019.
 * Havanah Team, ID.
 */
public class Order extends AppCompatActivity {

    ViewPager viewPager;
    PackageAdapter adapter;
    List<PackageModel> list;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        list = new ArrayList<>();
        list.add(new PackageModel(getResources().getString(R.string.package_daily), R.drawable.img_daily_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_lunch), R.drawable.img_lunch_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_specific), R.drawable.img_specific_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_weight_loss), R.drawable.img_weight_loss_package_slide));
        list.add(new PackageModel(getResources().getString(R.string.package_recommended), R.drawable.img_daily_package_slide));

        adapter = new PackageAdapter(list, this);
        viewPager = findViewById(R.id.viewPager_package);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0, 100, 0);

//        colors = new Integer[]{getResources().getColor(R.color.green_200),
//                getResources().getColor(R.color.red_200),
//                getResources().getColor(R.color.blue_200),
//                getResources().getColor(R.color.yellow_200)
//        };
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
//                    viewPager.setBackgroundColor(
//                            (Integer) argbEvaluator.evaluate(
//                                    positionOffset,
//                                    colors[position],
//                                    colors[position + 1]
//                            )
//                    );
//                } else {
//                    viewPager.setBackgroundColor(colors[colors.length - 1]);
//                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView_package);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setAnimationType(AnimationType.WORM);
    }
}
