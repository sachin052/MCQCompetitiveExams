package my.skrm.sachin.mcqcompetativeexams;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private CardView card1,card2,outercard,card4,card3; private AdView mAdView,adView;
    private static final int RC_SIGN_IN = 123;
    private FirebaseUser currentFirebaseUser;
    private DatabaseReference databaseReference;
    private TextView title;
    private LinearLayout l1,l2;
    private ImageView sponser;
    private List<AuthUI.IdpConfig> providers;
    private Animation animation;
    private ImageView imagView;
    private ProgressDialog pd;
    private InterstitialAd mInterstitialAd;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerLayout;
    private Button btn1,bt2,btn3,bt4;
    private ViewPager viewPager;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item)|| super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
           this.drawerLayout.closeDrawer(GravityCompat.START);

        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        Window window = getWindow();
        drawerLayout=findViewById(R.id.d1);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_head);
        title=headerLayout.findViewById(R.id.title);
        imagView=headerLayout.findViewById(R.id.profile);
        final Toolbar toolbar= findViewById(R.id.baseToolbar);
        sponser=toolbar.findViewById(R.id.main_spon);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout =  findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        sponser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dashboard");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int i=item.getItemId();
               if(i==R.id.privacy){
                   startActivity(new Intent(MainActivity.this,privacy.class));
                   return true;

               }
               else if(item.getItemId()==R.id.money){

                   if (mInterstitialAd.isLoaded()) {
                       mInterstitialAd.show();
                   }
                   return true;
               }
               else if(item.getItemId()==R.id.logout){
                   AuthUI.getInstance()
                           .signOut(MainActivity.this)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(MainActivity.this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(MainActivity.this,MainActivity.class));
                               }
                           });
                   return true;

               }
               else if(item.getItemId()==R.id.share){
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT,
                           "Hey Download this to take MCQs Test for FREE !! app at: https://play.google.com/store/apps/details?id="+getPackageName());
                   sendIntent.setType("text/plain");
                   startActivity(sendIntent);
                   return true;

               }
               return true;
           }
       });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0,96,100));
        }



        checkInternet();
       if(checkInternet()==1){
           FirebaseAuth.getInstance().getCurrentUser();
           MobileAds.initialize(this, "ca-app-pub-7443649607874529~6063001230");
           mAdView = findViewById(R.id.adView);
           AdRequest adRequest = new AdRequest.Builder().build();
           mAdView.loadAd(adRequest);
           mInterstitialAd = new InterstitialAd(this);
           mInterstitialAd.setAdUnitId("ca-app-pub-7443649607874529/8122204270");
           mInterstitialAd.loadAd(new AdRequest.Builder().build());
           currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
           databaseReference=FirebaseDatabase.getInstance().getReference();
           providers = Arrays.asList(
                   new AuthUI.IdpConfig.EmailBuilder().build(),
                   new AuthUI.IdpConfig.GoogleBuilder().build(),
                   new AuthUI.IdpConfig.FacebookBuilder().build()

           );

           if(currentFirebaseUser!=null){

               title.setText("  "+currentFirebaseUser.getDisplayName().toUpperCase());


                for(UserInfo profile : currentFirebaseUser.getProviderData()) {
               //     check if the provider id matches "facebook.com"
                 if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
               Glide.with(MainActivity.this).load(profile.getPhotoUrl()).into(imagView);
                }
                else {
                     if (currentFirebaseUser.getPhotoUrl()!=null) {
                         Glide.with(MainActivity.this).load(currentFirebaseUser.getPhotoUrl()).into(imagView);
                     }
                 }
               }
           }
           else{
               startActivityForResult(
                       AuthUI.getInstance()
                               .createSignInIntentBuilder()
                               .setAvailableProviders(providers)
                               .build(),
                       RC_SIGN_IN);

           }


       }
       else{
           Snackbar snackbar=Snackbar.make(l1,"No Internet Connection",Snackbar.LENGTH_LONG);
           View sbView = snackbar.getView();
           sbView.setBackgroundColor(Color.BLUE);
           snackbar.setActionTextColor(Color.WHITE);
           snackbar.show();
       }





}


    @Override
    public void onClick(View view) {
 try {
     if(view.getId()==R.id.main_spon){

         if (mInterstitialAd.isLoaded()) {
             mInterstitialAd.show();
         }
     }




 }catch (NumberFormatException ex){

     Toast.makeText(this, "Fetching Profile", Toast.LENGTH_SHORT).show();
 }

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               title.setText("  "+currentFirebaseUser.getDisplayName().toUpperCase());


                for(UserInfo profile : currentFirebaseUser.getProviderData()) {
                    //     check if the provider id matches "facebook.com"
                    if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                        Glide.with(MainActivity.this).load(profile.getPhotoUrl()).into(imagView);
                    }
                    else
                    {

                        Glide.with(MainActivity.this).load(currentFirebaseUser.getPhotoUrl()).into(imagView);
                    }
                }
            } else {

            }
        }
    }



   public int checkInternet(){
        int state=0;

       ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo info = connectivityManager.getActiveNetworkInfo();
       if ((info == null || !info.isConnected() || !info.isAvailable())) {

       }
       else{
           state=1;
           return  state;

       }
       return state;

   }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new homr_frag(), "Home");
        adapter.addFragment(new jobs(), "JOBS");
        adapter.addFragment(new Newsfrag(), "News");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}



