package id.havanah.app.dietonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Locale;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.model.SpecificPackageModel;
import id.havanah.app.dietonline.transaction.order.SpecificPackageTimesToSend;

/**
 * Created by farhan at 11:35
 * on 23/04/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageAdapter extends PagerAdapter {
    private List<SpecificPackageModel> list;
    private Context context;

    public SpecificPackageAdapter(List<SpecificPackageModel> list, Context context) {
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
        View view = inflater.inflate(R.layout.order_specific_package_menus_item, container, false);

//        ImageView image = view.findViewById(R.id.package_image);
        TextView name = view.findViewById(R.id.weightLossPackage_name);
        TextView price = view.findViewById(R.id.weightLossPackage_price);
        TextView desc1 = view.findViewById(R.id.weightLossPackage_desc1);
        TextView desc2 = view.findViewById(R.id.weightLossPackage_desc2);
        TextView desc3 = view.findViewById(R.id.weightLossPackage_desc3);
        TextView desc4 = view.findViewById(R.id.weightLossPackage_desc4);
        TextView desc5 = view.findViewById(R.id.weightLossPackage_desc5);
        TextView desc6 = view.findViewById(R.id.weightLossPackage_desc6);
        TextView desc7 = view.findViewById(R.id.weightLossPackage_desc7);
        TextView desc8 = view.findViewById(R.id.weightLossPackage_desc8);
        TextView desc9 = view.findViewById(R.id.weightLossPackage_desc9);
        Button btnChoose = view.findViewById(R.id.btn_chooseSpecificPackage);

        SpecificPackageModel model = list.get(position);

//        image.setImageResource(model.getImage());
        name.setText(model.getName());
        price.setText(String.format(Locale.US, "Rp %d", model.getPrice()));
        desc1.setText(model.getDesc1());
        desc2.setText(model.getDesc2());
        desc3.setText(model.getDesc3());
        desc4.setText(model.getDesc4());
        desc5.setText(model.getDesc5());
        desc6.setText(model.getDesc6());
        desc7.setText(model.getDesc7());
        desc8.setText(model.getDesc8());
        desc9.setText(model.getDesc9());

        btnChoose.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificPackageTimesToSend.class);
            intent.putExtra("product_id", model.getProduct_id());
            context.startActivity(intent);
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
