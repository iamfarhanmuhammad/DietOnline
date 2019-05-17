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

import androidx.recyclerview.widget.RecyclerView;
import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.model.ProductModel;
import id.havanah.app.dietonline.transaction.order.LunchPackageForward;

/**
 * Created by farhan at 01:07
 * on 12/04/2019.
 * Havanah Team, ID.
 */
public class LunchPackageAdapter extends RecyclerView.Adapter<LunchPackageAdapter.ViewHolder> {
    private final List<ProductModel> list = new ArrayList<>();

    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private Context context;

    public LunchPackageAdapter(Context context) {
        this.context = context;
        expansionsCollection.openOnlyOne(true);
    }

    @Override
    public LunchPackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LunchPackageAdapter.ViewHolder.buildFor(parent);
    }

    @Override
    public void onBindViewHolder(LunchPackageAdapter.ViewHolder holder, int position) {
        ProductModel productModelList = list.get(position);
        holder.image.setImageResource(productModelList.getImage());
        holder.name.setText(productModelList.getName());
        holder.price.setText(String.format(Locale.US, "Rp %d", productModelList.getPrice()));
        holder.content.setText(productModelList.getContent());
        holder.btnForward.setOnClickListener(v -> {
            Intent intent = new Intent(context, LunchPackageForward.class);
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

        private static final int LAYOUT = R.layout.order_lunch_package_item;
        private TextView name, price, content;
        private ImageView image, btnForward;

        ExpansionLayout expansionLayout;

        public static ViewHolder buildFor(ViewGroup viewGroup) {
            return new LunchPackageAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
        }

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.lunchPackage_image);
            name = itemView.findViewById(R.id.lunchPackage_name);
            price = itemView.findViewById(R.id.lunchPackage_price);
            content = itemView.findViewById(R.id.lunchPackage_content);
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
