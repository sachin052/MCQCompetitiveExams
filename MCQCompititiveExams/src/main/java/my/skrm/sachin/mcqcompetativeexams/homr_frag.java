package my.skrm.sachin.mcqcompetativeexams;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;


public class homr_frag extends Fragment implements View.OnClickListener {


    private CardView card1,card2,card3,card4;
    private Animation animation;
    private Button btn1,bt2,btn3,bt4;

    public homr_frag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_homr_frag, container, false);
        card1=v.findViewById(R.id.card1);
        card1.setOnClickListener(this);
        card2=v.findViewById(R.id.card2);
        card2.setOnClickListener(this);
        card3=v.findViewById(R.id.card3);
        card4=v.findViewById(R.id.card4);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        animation= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_bottom);
        card1.clearAnimation();
        card1.setAnimation(animation);
       card1.getAnimation().start();
        card2.clearAnimation();
        card2.setAnimation(animation);
        card2.getAnimation().start();
        card3.clearAnimation();
        card3.setAnimation(animation);
        card3.getAnimation().start();
        card4.clearAnimation();
        card4.setAnimation(animation);
        card4.getAnimation().start();
        btn1=v.findViewById(R.id.btn1);
        bt2=v.findViewById(R.id.btn2);
        btn3=v.findViewById(R.id.btn3);
        bt4=v.findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        bt4.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {


     try{if(view.getId()==R.id.card2||view.getId()==R.id.btn2){
         Intent intent = new Intent(getActivity().getApplicationContext(), category.class);
         intent.putExtra("id","100");
         intent.putExtra("name","COM");
         intent.putExtra("rq_code","2");
         startActivity(intent);

     }
     else if(view.getId()==R.id.card1 ||view.getId()==R.id.btn1){
         Intent intent = new Intent(getActivity().getApplicationContext(), category.class);
         intent.putExtra("id","101");
         intent.putExtra("name","CURRENT AFFAIRS");
         intent.putExtra("rq_code","1");
         startActivity(intent);


     }
     else if(view.getId()==R.id.card3 ||view.getId()==R.id.btn3){
         Intent intent = new Intent(getActivity().getApplicationContext(), category.class);
         intent.putExtra("id","102");
         intent.putExtra("name","STATIC GK");
         intent.putExtra("rq_code","3");
         startActivity(intent);


     }
     else if(view.getId()==R.id.card4||view.getId()==R.id.btn4){
         Intent intent = new Intent(getActivity().getApplicationContext(), category.class);
         intent.putExtra("id","103");
         intent.putExtra("name","GENERAL SCIENCE");
         intent.putExtra("rq_code","4");
         startActivity(intent);


     }
     }catch (NumberFormatException e){

         Toast.makeText(getActivity().getApplicationContext(), "Fetching Profile", Toast.LENGTH_SHORT).show();
     }
    }
}
