package my.skrm.sachin.mcqcompetativeexams;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
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


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class load_Selected_test extends AppCompatActivity {
    private  ListView l1;
    private String id,name;
    private DatabaseReference databaseReference;
    private ArrayList<String>arrayList;
    private myadapter adp;
    private AdView mAdView; private String cat_name;
    private FirebaseUser currentFirebaseUser;
    private int rqcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load__selected_test);
        l1=findViewById(R.id.test_list);
         adp = new myadapter(this);
        Intent intent = getIntent();
       id=intent.getStringExtra("id");
       name=intent.getStringExtra("name");
       rqcode=Integer.parseInt(intent.getStringExtra("rq_code"));
        arrayList= new ArrayList<>();
        MobileAds.initialize(this, "ca-app-pub-7443649607874529~6063001230");
        mAdView = findViewById(R.id.adView1);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(rqcode==0) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("SUB")
                    .child(id).child(name);
            new loadlist().execute();
        }
        else if(rqcode==1){
            cat_name=intent.getStringExtra("cat_name");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("SUB")
                    .child(id).child(name).child(cat_name);
            new loadlist().execute();

        }
        else if(rqcode==3){
            cat_name=intent.getStringExtra("cat_name");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("SUB")
                    .child(id).child(name).child(cat_name);
            arrayList.add("JANUARY");
            arrayList.add("FEBRUARY");
            arrayList.add("MARCH");
            arrayList.add("APRIL");
            arrayList.add("MAY");
            arrayList.add("JUNE");
            arrayList.add("JULY");
            arrayList.add("AUGUST");
            arrayList.add("SEPTEMBER");
            arrayList.add("OCTOBER");
            arrayList.add("NOVEMBER");
            arrayList.add("DECEMBER");
            adp.notifyDataSetChanged();

        }




        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0,96,100));
        }


        l1.setAdapter(adp);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Intent intent= new Intent(load_Selected_test.this,Tests.class);
                intent.putExtra("test_name",arrayList.get(i).toString());
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("cat_name",cat_name);
                intent.putExtra("rq_code",String.valueOf(rqcode));
                startActivity(intent);


            }
        });





    }



    class myadapter extends BaseAdapter {
        private Context mContext;

        // Keep all Images in array


        // Constructor
        public myadapter(Context c){
            mContext = c;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v;

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                v= new View(getApplicationContext());

            } else {
            v=convertView;
            }

            v = inflater.inflate(R.layout.load_test, null);
            TextView test_name=v.findViewById(R.id.test_name);
            ImageView play=v.findViewById(R.id.play);
            final TextView t_ques=v.findViewById(R.id.question_count);
            test_name.setText(arrayList.get(position));
            databaseReference.child(arrayList.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    t_ques.setText(String.valueOf(dataSnapshot.getChildrenCount())+" Questions");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Intent intent= new Intent(load_Selected_test.this,Tests.class);
                    intent.putExtra("test_name",arrayList.get(position).toString());
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("cat_name",cat_name);
                    intent.putExtra("rq_code",String.valueOf(rqcode));
                    startActivity(intent);


                }
            });
        return v;

        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }
    class loadlist extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
             progressDialog= new ProgressDialog(load_Selected_test.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for(DataSnapshot s:children){
                        arrayList.add(s.getKey());
                        adp.notifyDataSetChanged();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return  null;
        }
    }
}
