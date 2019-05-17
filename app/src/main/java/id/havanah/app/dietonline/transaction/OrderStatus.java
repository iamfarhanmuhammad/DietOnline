package id.havanah.app.dietonline.transaction;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.StatusPagerAdapter;
import id.havanah.app.dietonline.transaction.status.Done;
import id.havanah.app.dietonline.transaction.status.Paid;
import id.havanah.app.dietonline.transaction.status.Unpaid;

/**
 * Created by farhan at 17:13
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class OrderStatus extends AppCompatActivity {

    private StatusPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status_view);

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> onBackPressed());

        tabLayout = findViewById(R.id.tabLayout_orderStatus);
        viewPager = findViewById(R.id.viewPager_orderStatus);

        adapter = new StatusPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new Unpaid(), getResources().getString(R.string.unpaid));
        adapter.addFragment(new Paid(), getResources().getString(R.string.paid));
        adapter.addFragment(new Done(), getResources().getString(R.string.done));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0); // for initial selected tab view
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position); // for tab change
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }
}
