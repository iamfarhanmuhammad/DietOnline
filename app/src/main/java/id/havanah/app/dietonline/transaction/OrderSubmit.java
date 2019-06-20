package id.havanah.app.dietonline.transaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import id.havanah.app.dietonline.R;
import id.havanah.app.dietonline.api.ApiService;
import id.havanah.app.dietonline.helper.SQLiteHandler;

/**
 * Created by farhan at 14:51
 * on 12/04/2019.
 * Havanah Team, ID.
 */
public class OrderSubmit extends AppCompatActivity {

    private SQLiteHandler db;
    private String invoice;
    private ImageView proofImage;

    private static final String TAG = OrderSubmit.class.getSimpleName();

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_submit);

        //Requesting storage permission
        requestStoragePermission();

        ImageView btnBack = findViewById(R.id.home);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(OrderSubmit.this, OrderStatus.class));
            finish();
        });
        db = new SQLiteHandler(this);

        // fetch data
        int amount = Integer.parseInt(getIntent().getStringExtra("amount"));
        int price = Integer.parseInt(getIntent().getStringExtra("price"));
        invoice = getIntent().getStringExtra("invoice");
        int total = amount * price;
        TextView orderDetail = findViewById(R.id.order_detail);
        TextView orderAmount = findViewById(R.id.order_amount);
        orderDetail.setText(String.format(Locale.US, "Rp %d x %d", price, amount));
        orderAmount.setText(String.format(Locale.US, "Rp %d", total));

        // Declaring views
        CardView buttonChoose = findViewById(R.id.btn_choose);
        CardView buttonCapture = findViewById(R.id.btn_capture);
        Button buttonUpload = findViewById(R.id.btn_upload);
        Button buttonPending = findViewById(R.id.btn_pending);
        proofImage = findViewById(R.id.proof_image);
        buttonChoose.setOnClickListener(v -> showFileChooser());
        buttonUpload.setOnClickListener(v -> uploadImage(invoice));
        buttonPending.setOnClickListener(v -> {
            startActivity(new Intent(OrderSubmit.this, OrderStatus.class));
            finish();
        });
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadImage(String invoice) {

        if (isSelected(filePath)) {
            //getting the actual path of the image
            String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, ApiService.updatePaid)
                        .addFileToUpload(path, "proof") //Adding file
                        .addParameter("invoice", invoice) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, getResources().getString(R.string.processing_upload), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(context, "Error" + serverResponse.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Toast.makeText(context, getResources().getString(R.string.successfully_uploaded), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OrderSubmit.this, OrderStatus.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, getResources().getString(R.string.successfully_cancelled), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.notice_no_image_chosen), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.choose_image)), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Bitmap to get image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                proofImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSelected(Uri uri) {
        return uri != null;
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
