package my.skrm.sachin.mcqcompetativeexams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class splash extends AppCompatActivity {
    private TextView version;
    private Animation animation, anim;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0,96,100));
        }

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String versionCode = String.valueOf(info.versionCode);
        version=findViewById(R.id.version);
        version.setText("Version : "+versionCode);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        int i = checkInternet();
        if(i==1){

            databaseReference.child("VER").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(versionCode.equals(dataSnapshot.getValue().toString())){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(splash.this,MainActivity.class));
                                finish();
                            }
                        },3000);

                    }
                    else {

                        new AlertDialog.Builder(splash.this).setMessage("Please update your app!!")
                                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setCancelable(false).setTitle("Update Available").create().show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

}
