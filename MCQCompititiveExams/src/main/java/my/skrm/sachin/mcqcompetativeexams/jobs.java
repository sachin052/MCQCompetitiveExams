package my.skrm.sachin.mcqcompetativeexams;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class jobs extends Fragment {
    ArrayList<String> titles;
    ArrayList<String> desc;
    ArrayList<String> link;
    private ArrayList<String> newarray;
    customadapter customadapter;

    public jobs() {
        titles=new ArrayList<>();
        desc=new ArrayList<>();
        link=new ArrayList<>();
        newarray =new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.fragment_jobs, container, false);
        ListView listView=v.findViewById(R.id.job_list);
        customadapter= new customadapter(newarray);
        listView.setAdapter(customadapter);
        EditText searchView=v.findViewById(R.id.jobsearch);

        String urlString = "http://www.freshersbazaar.com/rss-feeds/central-govt-jobs-rssfeed.xml";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                for(Article s:list){
                    titles.add(s.getTitle().toLowerCase());
                    newarray.add(s.getTitle());
                    desc.add(s.getDescription().trim());
                    link.add(s.getLink());

                }
                customadapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
       return v;
    }
    class customadapter extends BaseAdapter  {


        private ArrayList<String> temparray;
        ArrayList<String> orignal;

        public customadapter(ArrayList<String> inter) {
            this.temparray = titles;
            this.orignal = inter;

        }

        @Override
        public int getCount() {
            return temparray.size();
        }

        @Override
        public Object getItem(int i) {
            return temparray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return temparray.indexOf(getItemId(i));
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v;
            final int pos = i;
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                v = new View(getActivity().getApplicationContext());

            } else {


                v = view;


            }
            v = inflater.inflate(R.layout.joblayout, null);
            TextView test_name = v.findViewById(R.id.jobtitle);
            TextView desc_text = v.findViewById(R.id.jobdesc);
            TextView readjob = v.findViewById(R.id.readjob);
            CardView card = v.findViewById(R.id.jobcard);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(link.get(pos));
                }
            });
            readjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(link.get(pos));
                }
            });
            test_name.setText(titles.get(i));
            desc_text.setText(desc.get(i));

            return v;

        }


    }
    public void openLink(String link){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(myIntent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
