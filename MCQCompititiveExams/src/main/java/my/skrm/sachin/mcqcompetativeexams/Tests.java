package my.skrm.sachin.mcqcompetativeexams;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class Tests extends AppCompatActivity  {
    private String name,id,test_name,cat_name,current_ques;
    private DatabaseReference databaseReference;
    private ArrayList<String> questions,option;
    private TextView question;
    private LinearLayout layout;
    private Button ans1,ans2,ans3,ans4,skip,finish;
    int count=0,options=0,right_answer=0,wrong_asnwer=0,total_question=1,skip_question=0,rqcode;
    private LinearLayout l1;
    private AdView mAdView;
    private adapter adapter;
    private CountDownTimer start;
    private TextView right,wrong,question_rec,timer;
    private MediaPlayer mediaPlayer;
    private Animation animation;
    HashMap<String, String> answers;
    private ListView listView;
    int track=-1;
    private HashMap<Integer,Integer>mSelectedItem;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Tests.this).setMessage("Do you want to leave the test?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("No",null).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        layout=findViewById(R.id.main_layout);
        test_name=intent.getStringExtra("test_name");
        rqcode=Integer.parseInt(intent.getStringExtra("rq_code"));
        animation=AnimationUtils.loadAnimation(Tests.this,R.anim.anim_slide_in_left);
        MobileAds.initialize(this, "ca-app-pub-7443649607874529~6063001230");
        mAdView = findViewById(R.id.adView2);
        timer=findViewById(R.id.timer);
        AdRequest adRequest = new AdRequest.Builder().build();
        questions=new ArrayList<>();
        mSelectedItem=new HashMap<>();

        option=new ArrayList<>();
        answers=new HashMap<>();
        mAdView.loadAd(adRequest);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0,96,100));
        }
        question=findViewById(R.id.question);
        question_rec=findViewById(R.id.ques_record);



     if(rqcode==0){
         databaseReference= FirebaseDatabase.getInstance().getReference().child("SUB")
                 .child(id).child(name).child(test_name);

     }
     else if(rqcode==1){
        cat_name=intent.getStringExtra("cat_name");
         String test=intent.getStringExtra("test_name");
         databaseReference = FirebaseDatabase.getInstance().getReference().child("SUB")
                 .child(id).child(name).child(cat_name).child(test);
     }
     else if(rqcode==3){
         cat_name=intent.getStringExtra("cat_name");
         String test=intent.getStringExtra("test_name");
         databaseReference = FirebaseDatabase.getInstance().getReference().child("SUB")
                 .child(id).child(name).child(cat_name).child(test);

        }
        listView=findViewById(R.id.testlist);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        adapter=new adapter(this);
        listView.setAdapter(adapter);
        new loadasyn().execute();
        loadTimer();





}

    public void loadTimer(){

         start= new CountDownTimer(60 * 10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = (millisUntilFinished / 60000);
                long min = (millisUntilFinished % 60000 / 1000);
                timer.setText(String.valueOf(hours) + " : " + String.valueOf(min));

            }

            @Override
            public void onFinish() {

                Toast.makeText(Tests.this, "Time is over", Toast.LENGTH_SHORT).show();

            }
        }.start();
    }









public class adapter extends BaseAdapter  {
    private Context mContext;
    LinearLayout layout1,layout2,layout3,layout4;

    public adapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override

    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        if (view == null) {
            v= new View(getApplicationContext());

        }

        else {
            v=view;
        }
        v = inflater.inflate(R.layout.test_layout, null);
        TextView questionno=v.findViewById(R.id.questionno);
        TextView question=v.findViewById(R.id.question);
        TextView option1=v.findViewById(R.id.newoption1);
        TextView option2=v.findViewById(R.id.newoption2);
        TextView option3=v.findViewById(R.id.newoption3);
        TextView option4=v.findViewById(R.id.newoption4);
        question.setText(questions.get(i));
        option1.setText(option.get(i));
        option2.setText(option.get(i+1));
        option3.setText(option.get(i+2));
        option4.setText(option.get(i+3));
        questionno.setText("Q."+String.valueOf(i+1));
        layout1=v.findViewById(R.id.option1);
        layout2=v.findViewById(R.id.option2);
        layout3=v.findViewById(R.id.option3);
        layout4=v.findViewById(R.id.option4);
        TextView img1=v.findViewById(R.id.answerimg1);
        TextView img2=v.findViewById(R.id.answerimg2);
        TextView img3=v.findViewById(R.id.answerimg3);
        TextView img4=v.findViewById(R.id.answerimg4);
        if(track!=-1){

        if(mSelectedItem.containsKey(i)){
            if(mSelectedItem.get(i).equals(1)){

                layout1.setBackgroundResource(R.drawable.selectedoption);
                img1.setBackgroundResource(R.drawable.answercircledark);
                img1.setTextColor(Color.WHITE);
            }
            if(mSelectedItem.get(i).equals(2)){

                layout2.setBackgroundResource(R.drawable.selectedoption);
                img2.setBackgroundResource(R.drawable.answercircledark);
                img2.setTextColor(Color.WHITE);
            }
            if(mSelectedItem.get(i).equals(3)){

                layout3.setBackgroundResource(R.drawable.selectedoption);
                img3.setBackgroundResource(R.drawable.answercircledark);
                img3.setTextColor(Color.WHITE);
            }
            if(mSelectedItem.get(i).equals(4)){

                layout4.setBackgroundResource(R.drawable.selectedoption);
                img4.setBackgroundResource(R.drawable.answercircledark);
                img4.setTextColor(Color.WHITE);
            }



        }




        }

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSelectedItem.put(i,1);
                track=i;
                adapter.notifyDataSetChanged();

            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem.put(i,2);
                track=i;
                adapter.notifyDataSetChanged();
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem.put(i,3);
                track=i;
                adapter.notifyDataSetChanged();
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSelectedItem.put(i,4);
                track=i;
                adapter.notifyDataSetChanged();
            }
        });






        return v;

    }



}
public class loadasyn extends AsyncTask<Void,Void,Void>{
    @Override
    protected void onPostExecute(Void aVoid) {

    }

    @Override
    protected Void doInBackground(Void... voids) {
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    questions = new ArrayList<>();
                    option = new ArrayList<>();
                    answers = new HashMap<>();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot d : children) {
                        questions.add(d.getKey());
                        adapter.notifyDataSetChanged();
                        for (DataSnapshot ss : d.getChildren()) {

                            option.add(ss.getKey());
                            if (ss.getValue().toString().equals("RIGHT")) {

                                answers.put(d.getKey(), ss.getKey());
                            }

                        }

                    }

                }catch(Exception e){
                    Toast.makeText(Tests.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return null;
    }
}

}
