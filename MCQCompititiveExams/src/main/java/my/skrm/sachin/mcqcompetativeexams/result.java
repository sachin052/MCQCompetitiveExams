package my.skrm.sachin.mcqcompetativeexams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class result extends AppCompatActivity {

    private AdView mAdView;
    private  String total_ques,skiped;
    private TextView ques,attempted,accuracy,timer,right;
    private int per;
    private Animation animation;
    private TextView textView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        try {
            MobileAds.initialize(this, "ca-app-pub-7443649607874529~6063001230");
            mAdView = findViewById(R.id.ad_result);
            Intent intent = getIntent();
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            ques = findViewById(R.id.total_ques);
            attempted = findViewById(R.id.attempted);
            accuracy = findViewById(R.id.accuracy);
            timer = findViewById(R.id.time_taken);
            right = findViewById(R.id.correct_ans);
            Toolbar toolbar = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            Window window = getWindow();
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-7443649607874529/8122204270");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    showInterstitial();
                }

            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.rgb(0, 96, 100));
            }

            total_ques = intent.getStringExtra("total_ques");
            skiped = intent.getStringExtra("skip_question");

            ques.setText(total_ques);
            attempted.setText(String.valueOf((Integer.parseInt(total_ques) - Integer.parseInt(skiped))));
            int att = (Integer.parseInt(total_ques) - Integer.parseInt(skiped));

            if (att == 0) {
                accuracy.setText("0%");
                right.setText("0");
            } else {
                int t = Integer.parseInt(total_ques);
                int correct = Integer.parseInt(intent.getStringExtra("correct_ans"));
                per = (correct * 100) / t;
                accuracy.setText(String.valueOf(per) + "%");
                right.setText(intent.getStringExtra("correct_ans"));
            }
            timer.setText(intent.getStringExtra("timer"));

        }catch (Exception e){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            finish();

        }
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else{

            startActivity(new Intent(this,result.class));
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.money) {
            showInterstitial();
            return  true;
        }
        else if(item.getItemId()==R.id.logout){
            AuthUI.getInstance()
                    .signOut(result.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(result.this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(result.this,MainActivity.class));
                        }
                    });
            return  true;

        }
        else if(item.getItemId()==R.id.share){
            ShareCompat.IntentBuilder.from(result.this)
                    .setType("text/plain")
                    .setChooserTitle("Share this App")
                    .setText("http://play.google.com/store/apps/details?id="+this.getPackageName())
                    .startChooser();

        }
        else if(item.getItemId()==R.id.privacy){
            startActivity(new Intent(result.this,privacy.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
