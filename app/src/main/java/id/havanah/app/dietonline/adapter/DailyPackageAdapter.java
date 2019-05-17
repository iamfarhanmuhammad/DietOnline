package id.havanah.app.dietonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.model.ProductModel;
import id.havanah.app.dietonline.transaction.order.DailyPackageForward;

/**
 * Created by farhan at 22:42
 * on 11/04/2019.
 * Havanah Team, ID.
 */
public class DailyPackageAdapter extends RecyclerView.Adapter<DailyPackageAdapter.ViewHolder> {
    private final List<ProductModel> list = new ArrayList<>();

    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private Context context;

    public DailyPackageAdapter(Context context) {
        this.context = context;
        expansionsCollection.openOnlyOne(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DailyPackageAdapter.ViewHolder.buildFor(parent);
    }

    @Override
    public void onBindViewHolder(DailyPackageAdapter.ViewHolder holder, int position) {
        ProductModel productModelList = list.get(position);
        holder.image.setImageResource(productModelList.getImage());
        holder.name.setText(productModelList.getName());
        holder.price.setText(String.format(Locale.US, "Rp %d", productModelList.getPrice()));
        holder.content.setText(productModelList.getContent());
        holder.btnForward.setOnClickListener(v -> {
            Intent intent = new Intent(context, DailyPackageForward.class);
            intent.putExtra("product_id", productModelList.getProduct_id());
            intent.putExtra("name", productModelList.getName());
            intent.putExtra("price", productModelList.getPrice());
            intent.putExtra("image", productModelList.getImage());
            context.startActivity(intent);
        });
        holder.bind(list.get(position));
        expansionsCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<ProductModel> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT = R.layout.order_daily_package_item;
        private TextView name, price, content;
        private ImageView image, btnForward;

        ExpansionLayout expansionLayout;

        public static DailyPackageAdapter.ViewHolder buildFor(ViewGroup viewGroup) {
            return new DailyPackageAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
        }

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.dailyPackage_image);
            name = itemView.findViewById(R.id.dailyPackage_name);
            price = itemView.findViewById(R.id.dailyPackage_price);
            content = itemView.findViewById(R.id.dailyPackage_content);
            btnForward = itemView.findViewById(R.id.btn_forwardPlan);
            expansionLayout = itemView.findViewById(R.id.expansionLayout);
        }

        public void bind(ProductModel object) {
            expansionLayout.collapse(false);
        }

        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }
    }
}
