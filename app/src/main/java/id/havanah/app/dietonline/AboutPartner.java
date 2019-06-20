package id.havanah.app.dietonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by farhan at 23:39
 * on 03/05/2019.
 * Havanah Team, ID.
 */
public class AboutPartner extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_partner);

    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

    public void goToWeb(View view) {
        goToUrl("http://www.dietindo.com");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
