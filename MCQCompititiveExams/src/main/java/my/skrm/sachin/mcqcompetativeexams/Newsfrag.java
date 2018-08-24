package my.skrm.sachin.mcqcompetativeexams;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.RSSUtils;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Newsfrag extends Fragment {
    customadapter customadapter;
    ArrayList<String> titles;
    ArrayList<String> imgarray;
    ArrayList<String> pubdate;
    ArrayList<String> link;
    public Newsfrag() {

        titles=new ArrayList<>();
        imgarray=new ArrayList<>();
        pubdate=new ArrayList<>();
        link=new ArrayList<>();

    }   


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_newsfrag, container, false);
        ListView listView=v.findViewById(R.id.news_list);
        final EditText search=v.findViewById(R.id.newsearch);
        customadapter=new customadapter();
        listView.setAdapter(customadapter);
        String urlString = "https://www.bhaskar.com/rss-feed/2322/";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                for(Article s:list){
                titles.add(s.getTitle());
                imgarray.add(s.getImage());
                pubdate.add(s.getPubDate().toString().replace("GMT+05:30 2018",""));
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



    class customadapter extends BaseAdapter {




        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int i) {
            return titles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v;

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                v= new View(getActivity().getApplicationContext());

            }
            else{


                    v = view;


            }
            v = inflater.inflate(R.layout.feedlayout, null);
            TextView test_name = v.findViewById(R.id.newstitle);
            TextView date = v.findViewById(R.id.pubdate);
            ImageView img=v.findViewById(R.id.feedimage);
            TextView readNews=v.findViewById(R.id.readnews);
            CardView card=v.findViewById(R.id.newscard);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLink(link.get(i));
                }
            });
            readNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                openLink(link.get(i));

                }
            });
            test_name.setText(titles.get(i));
            date.setText(pubdate.get(i));
            Picasso.get().load(imgarray.get(i))
                    .into(img);

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

