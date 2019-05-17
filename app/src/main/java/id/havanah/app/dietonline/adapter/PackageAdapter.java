package id.havanah.app.dietonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.model.PackageModel;
import id.havanah.app.dietonline.transaction.order.DailyPackageView;
import id.havanah.app.dietonline.transaction.order.LunchPackageView;
import id.havanah.app.dietonline.transaction.order.RecommendedPackageView;
import id.havanah.app.dietonline.transaction.order.SpecificPackageView;
import id.havanah.app.dietonline.transaction.order.WeightLossPackageView;

/**
 * Created by farhan at 21:09
 * on 12/04/2019.
 * Havanah Team, ID.
 */
public class PackageAdapter extends PagerAdapter {

    private List<PackageModel> list;
    private Context context;

    public PackageAdapter(List<PackageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, container, false);

        CardView layout = view.findViewById(R.id.cardView_toPackageChoose);
        ImageView image = view.findViewById(R.id.package_image);
        TextView name = view.findViewById(R.id.package_name);

        PackageModel model = list.get(position);

        image.setImageResource(model.getImage());
        name.setText(model.getName());
        layout.setOnClickListener(v -> {
            switch (position) {
                case 0:
                    context.startActivity(new Intent(context, DailyPackageView.class));
                    break;
                case 1:
                    context.startActivity(new Intent(context, LunchPackageView.class));
                    break;
                case 2:
                    context.startActivity(new Intent(context, SpecificPackageView.class));
                    break;
                case 3:
                    context.startActivity(new Intent(context, WeightLossPackageView.class));
                    break;
                case 4:
                    context.startActivity(new Intent(context, RecommendedPackageView.class));
                    break;
                default:
                    Toast.makeText(context, "The package is unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
