package id.havanah.app.dietonline.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.RandomAccess;
import java.util.Set;

/**
 * Created by farhan at 16:13
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixels(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public int getScreenHeight() {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public int getStatusBarHeight() {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    // Convert Array of Integer  to string
    public String convertIntArrayToString(int[] intArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : intArray) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    // Convert Set of Integer to Array of Integer
    public int[] convertSetIntegerToIntArray(Set<Integer> setInteger) {
        int[] arr = new int[setInteger.size()];
        int index = 0;
        for (Integer i : setInteger) {
            arr[index++] = i;
        }
        return arr;
    }

    public int[] convertSetIntegerToIntArray(Set<Integer> setInteger, int param) {
        int[] arr = new int[setInteger.size()];
        int index = 0;
        for (Integer i : setInteger) {
            arr[index++] = i + param;
        }
        return arr;
    }

    // List<Integer> to int[]
    public int[] convertListIntegerToIntArray(List<Integer> listResult) {
        int size = listResult.size();
        int[] result = new int[size];
        if (listResult instanceof RandomAccess) {
            for (int i = 0; i < size; i++) {
                result[i] = listResult.get(i);
            }
        } else {
            int i = 0;
            for (int num : listResult) {
                result[i++] = num;
            }
        }
        return result;
    }

    public int[] convertListIntegerToIntArray(List<Integer> listResult, int param) {
        int size = listResult.size();
        int[] result = new int[size];
        if (listResult instanceof RandomAccess) {
            for (int i = 0; i < size; i++) {
                result[i] = listResult.get(i) + param;
            }
        } else {
            int i = 0;
            for (int num : listResult) {
                result[i++] = num + param;
            }
        }
        return result;
    }

    // Check email validity
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Get Uri Of captured Image
    public static Uri getOutputMediaFileUri(Context context) {
        File mediaStorageDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Camera");
        //If File is not present create directory
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());//Get Current timestamp
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");//create image path with system mill and image format
        return Uri.fromFile(mediaFile);

    }

    /*  Convert Captured image path into Bitmap to display over ImageView  */
    public static Bitmap convertImagePathToBitmap(String imagePath, boolean scaleBitmap) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);//Decode image path

        //If you want to scale bitmap/reduce captured image size then send true
        if (scaleBitmap)
            return Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        else
            //if you don't want to scale bitmap then send false
            return bitmap;
    }
}
