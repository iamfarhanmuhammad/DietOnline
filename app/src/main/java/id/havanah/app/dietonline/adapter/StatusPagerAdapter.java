package id.havanah.app.dietonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import id.havanah.app.dietonline.R;

/**
 * Created by farhan at 18:30
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class StatusPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;

    public StatusPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_status_tab, null);
        TextView tabTextView = view.findViewById(R.id.textView_tabOrder);
        tabTextView.setText(mFragmentTitleList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_status_tab, null);
        TextView tabTextView = view.findViewById(R.id.textView_tabOrder);
        tabTextView.setText(mFragmentTitleList.get(position));
        tabTextView.setTextSize(16);
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        return view;
    }
}
