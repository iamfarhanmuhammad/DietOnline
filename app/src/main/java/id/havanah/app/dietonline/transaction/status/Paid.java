package id.havanah.app.dietonline.transaction.status;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.StatusAdapter;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.helper.RecyclerItemTouchHelper;
import id.havanah.app.dietonline.model.StatusModel;
import id.havanah.app.dietonline.transaction.OrderStatus;

/**
 * Created by farhan at 18:37
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class Paid extends Fragment {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private StatusAdapter adapter;
    private List<StatusModel> list;
    private String TAG = Paid.class.getSimpleName();
    private FloatingActionButton btnExtend;
    private static int size;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_paid_view, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        initRecyclerView(view);

        RecyclerItemTouchHelper swipeHelper = new RecyclerItemTouchHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<RecyclerItemTouchHelper.UnderlayButton> underlayButtons) {
                underlayButtons.add(new RecyclerItemTouchHelper.UnderlayButton(
                        getContext().getResources().getString(R.string.edit_note),
                        R.drawable.ic_edit_black_24dp,
                        Color.parseColor("#FFD700"),
                        pos -> initPopup(list.get(pos).getId())
                ));
            }
        };

        fetchData();
        initExtendButton(view);
        return view;
    }

    private void initRecyclerView(View view) {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_paidStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void initExtendButton(View view) {
        btnExtend = view.findViewById(R.id.btn_extend);
        if (size <= 3 && size >= 1) {
            btnExtend.setOnClickListener(v -> Toast.makeText(getContext(), "Extended", Toast.LENGTH_SHORT).show());
        } else {
            btnExtend.setOnClickListener(v -> Toast.makeText(getContext(), getResources().getString(R.string.notice_extend_order), Toast.LENGTH_SHORT).show());
        }
    }

    private void setSize(int s) {
        size = s;
    }

    public int getSize() {
        return size;
    }

    private void initPopup(String uid) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.order_status_item_edit_note_popup);
        EditText inputNewNote = dialog.findViewById(R.id.input_newNote);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            String newNote = inputNewNote.getText().toString();
            progressDialog.setMessage(getContext().getResources().getString(R.string.updating));
            showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.updateNote,
                    response -> {
                        hideDialog();
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.successfully_updated), Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).finish();
                        getContext().startActivity(new Intent(getContext(), OrderStatus.class));
                    },
                    error -> {
                        hideDialog();
                        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).finish();
                        getContext().startActivity(new Intent(getContext(), OrderStatus.class));
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("notes", newNote);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        });
        dialog.show();
    }

    private void fetchData() {
        UserData userData = new UserData();
        String user_id = userData.getUid();
        final String taq_request = "request";
        progressDialog.setMessage(getResources().getString(R.string.fetching_data));
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiService.fetchTransactionData, response -> {
            hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
                    JSONArray transaction = new JSONArray(jsonObject.getString("transaction"));
                    for (int i = 0; i < transaction.length(); i++) {
                        JSONObject object = transaction.getJSONObject(i);
                        list.add(new StatusModel(object.getString("id"),
                                object.getString("invoice"),
                                object.getString("product_id"),
                                object.getString("date"),
                                object.getString("times"),
                                object.getString("notes"),
                                object.getString("status")
                        ));
                        adapter = new StatusAdapter(list, getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                    setSize(list.size());
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            hideDialog();
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("status", "3");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, taq_request);
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
