package my.skrm.sachin.mcqcompetativeexams;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class category extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ListView list;
    private customAdapter customAdapter;
    private String id,name;
    private DatabaseReference databaseReference;
    private ProgressDialog Dialog;
    private FirebaseUser currentFirebaseUser;
    private AdView mAdView;
    private String rqcode;
    private SpotsDialog spotsDialog;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        arrayList=new ArrayList<>();
        list=findViewById(R.id.cat_list);
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        rqcode=intent.getStringExtra("rq_code");


        MobileAds.initialize(this, "ca-app-pub-7443649607874529~6063001230");
        mAdView = findViewById(R.id.cat_ad);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0,96,100));
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        customAdapter= new customAdapter(this);
        list.setAdapter(customAdapter);
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if(arrayList.get(i).equals("IMPORTANT DAYS")){

                  final Intent intent = new Intent(category.this, load_Selected_test.class);
                  intent.putExtra("cat_name", "IMPORTANT DAYS");
                  intent.putExtra("id", "102");
                  intent.putExtra("name", "STATIC GK");
                  intent.putExtra("rq_code", "3");
                  startActivity(intent);

              } else {
                  final Intent intent = new Intent(category.this, load_Selected_test.class);
                  intent.putExtra("cat_name", arrayList.get(i).toString());
                  intent.putExtra("id", id);
                  intent.putExtra("name", name);
                  intent.putExtra("rq_code", "1");
                  startActivity(intent);
              }

          }
      });
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("SUB")
                .child(id).child(name);
        if(rqcode.equals("1")){
            arrayList.add("JANUARY");
            arrayList.add("FEBRUARY");
            arrayList.add("MARCH");
            arrayList.add("APRIL");
            arrayList.add("MAY");
            arrayList.add("JUNE");
            arrayList.add("JULY");
            arrayList.add("AUGUST");
            customAdapter.notifyDataSetChanged();
        }
        else if(rqcode.equals("2") || rqcode.equals("3") || rqcode.equals("4")){

            new loaddata().execute();
        }

    }
    class customAdapter extends BaseAdapter {
        private Context mContext;

        public customAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override

        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v = new View(mContext);

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                v = inflater.inflate(R.layout.cat_layout, null);
                TextView test_name = v.findViewById(R.id.load_cat);
                ImageView play = v.findViewById(R.id.pla3);
                final TextView question_count=v.findViewById(R.id.question_count);
                test_name.setText(arrayList.get(i).toString());
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (arrayList.get(i).equals("IMPORTANT DAYS")) {
                            final Intent intent = new Intent(category.this, load_Selected_test.class);
                            intent.putExtra("cat_name", arrayList.get(i).toString().toString());
                            intent.putExtra("id", "102");
                            intent.putExtra("name", "STATIC GK");
                            intent.putExtra("rq_code", "3");
                            startActivity(intent);

                        } else {
                            final Intent intent = new Intent(category.this, load_Selected_test.class);
                            intent.putExtra("cat_name", arrayList.get(i).toString().toString());
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("rq_code", "1");
                            startActivity(intent);
                        }
                    }
                });

            }
            else{

                v = view;
            }

            return v ;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class loaddata extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(category.this, "Loading", Toast.LENGTH_SHORT).show();
        }




        @Override
        protected void onPostExecute(Void aVoid) {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

        }

        @Override
        protected void onPreExecute() {
            ShowLoading();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    arrayList.add(dataSnapshot.getKey().toString());
                    customAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
    private void ShowLoading(){
        mProgressDialog = new ProgressDialog(category.this);
        //mProgressDialog.setMessage("Loading Please wait ....");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading data");
        mProgressDialog.show();

    }
}
