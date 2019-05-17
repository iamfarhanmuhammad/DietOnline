package id.havanah.app.dietonline.transaction.status;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.adapter.StatusAdapter;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.auth.UserData;
import id.havanah.app.dietonline.model.StatusModel;

/**
 * Created by farhan at 18:39
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class Done extends Fragment {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private StatusAdapter adapter;
    private List<StatusModel> list;
    private String TAG = Done.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_done_view, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        initRecyclerView(view);
        fetchData();
        return view;
    }

    private void initRecyclerView(View view) {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_doneStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
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
    }

    private void hideDialog() {
    }
}
