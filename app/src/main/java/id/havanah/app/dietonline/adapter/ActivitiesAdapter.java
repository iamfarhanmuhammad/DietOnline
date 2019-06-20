package id.havanah.app.dietonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.model.ActivitiesModel;

/**
 * Created by farhan at 12:42
 * on 04/06/2019.
 * Havanah Team, ID.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ActivitiesModel> list;
    private int checkedPosition = -1;

    public ActivitiesAdapter(Context context, ArrayList<ActivitiesModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setActivity(ArrayList<ActivitiesModel> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_specific_package_activities_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle;
        private CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView_activity);
            title = itemView.findViewById(R.id.textView_activityTitle);
            subtitle = itemView.findViewById(R.id.textView_activitySubtitle);
        }

        void bind(ActivitiesModel model) {
            if (checkedPosition == -1) {
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green_400));
                } else {
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }

            title.setText(model.getTitle());
            subtitle.setText(model.getSubtitle());

            itemView.setOnClickListener(view -> {
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green_200));
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
            });
        }
    }

    public ActivitiesModel getSelected() {
        if (checkedPosition != -1) {
            return list.get(checkedPosition);
        }
        return null;
    }
}
