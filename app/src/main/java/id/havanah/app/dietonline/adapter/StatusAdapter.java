package id.havanah.app.dietonline.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.model.StatusModel;
import id.havanah.app.dietonline.transaction.OrderStatus;
import id.havanah.app.dietonline.transaction.OrderSubmit;

import static id.havanah.app.dietonline.app.AppController.getContext;

/**
 * Created by farhan at 21:25
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private List<StatusModel> list;
    private Context context;
    private ProgressDialog progressDialog;

    public StatusAdapter(List<StatusModel> list, Context context) {
        this.list = list;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_status_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StatusModel statusModel = list.get(position);
        holder.invoice.setText(statusModel.getInvoice());
        if (Integer.parseInt(statusModel.getStatus()) == 1) {
            holder.status.setText(context.getResources().getString(R.string.unpaid));
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.btnAction.setBackgroundResource(R.drawable.shape_capsule_red);
            holder.btnAction.setText(context.getResources().getString(R.string.pay));
            holder.btnAction.setOnClickListener(v -> {
                progressDialog.setMessage(context.getResources().getString(R.string.loading));
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.fetchByInvoice, response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Intent intent = new Intent(context, OrderSubmit.class);
                        intent.putExtra("invoice", statusModel.getInvoice());
                        if (jsonObject.getString("product_id").equalsIgnoreCase("WL001")) {
                            intent.putExtra("amount", "1");
                        } else {
                            intent.putExtra("amount", jsonObject.getString("amount"));
                        }
                        intent.putExtra("price", jsonObject.getString("price"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("invoice", statusModel.getInvoice());
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
            });
            holder.ticketHeader.setCardBackgroundColor(context.getResources().getColor(R.color.red));
            holder.ticketBody.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (Integer.parseInt(statusModel.getStatus()) == 2) {
            holder.status.setText(context.getResources().getString(R.string.paid));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.btnAction.setBackgroundResource(R.drawable.shape_capsule_green);
            holder.btnAction.setText(context.getResources().getString(R.string.accept));
            holder.btnAction.setOnClickListener(v -> {
                confirmReceived(statusModel.getId());
                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
            });
            holder.ticketHeader.setCardBackgroundColor(context.getResources().getColor(R.color.green));
            holder.ticketBody.setCardBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            holder.status.setText(context.getResources().getString(R.string.done));
            holder.status.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.btnAction.setVisibility(View.GONE);
            holder.ticketHeader.setCardBackgroundColor(context.getResources().getColor(R.color.black));
            holder.ticketBody.setCardBackgroundColor(context.getResources().getColor(R.color.black));
        }
        switch (statusModel.getProduct_id()) {
            case "DP001":
                holder.name.setText("Katering Harian - Personal");
                break;
            case "DP002":
                holder.name.setText("Katering Harian - Family 2");
                break;
            case "DP003":
                holder.name.setText("Katering Harian - Family 3");
                break;
            case "SL001":
                holder.name.setText("Single Lunch Box - Puas Aja");
                break;
            case "SL002":
                holder.name.setText("Single Lunch Box - Puas Banget");
                break;
            case "SP001":
                holder.name.setText("Paket Spesial - Silver");
                break;
            case "SP002":
                holder.name.setText("Paket Spesial - Gold");
                break;
            case "SP003":
                holder.name.setText("Paket Spesial - Platinum");
                break;
        }
        String stringDate = statusModel.getDate();
        String[] date = stringDate.split("-");
        holder.date.setText(String.format("%s-%s-%s", date[2], date[1], date[0]));
        switch (statusModel.getTime()) {
            case "1":
                holder.time.setText(context.getResources().getString(R.string.breakfast));
                break;
            case "2":
                holder.time.setText(context.getResources().getString(R.string.lunch));
                break;
            case "3":
                holder.time.setText(context.getResources().getString(R.string.dinner));
                break;
        }
        holder.notes.setText(statusModel.getNotes());
        String invoice = statusModel.getInvoice();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(invoice, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            holder.barcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView ticketHeader, ticketBody;
        TextView invoice, status, name, date, time, notes;
        ImageView barcode;
        Button btnAction;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketHeader = itemView.findViewById(R.id.cardView_ticketHeader);
            ticketBody = itemView.findViewById(R.id.cardView_ticketBody);
            invoice = itemView.findViewById(R.id.textView_transactionId);
            status = itemView.findViewById(R.id.textView_transactionStatus);
            name = itemView.findViewById(R.id.textView_productName);
            date = itemView.findViewById(R.id.textView_dateToSend);
            time = itemView.findViewById(R.id.textView_timeToSend);
            notes = itemView.findViewById(R.id.textView_notes);
            barcode = itemView.findViewById(R.id.barcode_invoice);
            btnAction = itemView.findViewById(R.id.btn_transactionAction);
        }
    }

    private void confirmReceived(String uid) {
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.updateDone, response -> {
            hideDialog();
            Toast.makeText(context, context.getResources().getString(R.string.confirmed), Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, OrderStatus.class));
        }, error -> {
            hideDialog();
            Toast.makeText(context, context.getResources().getString(R.string.failed_confirmation), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("uid", uid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
