package com.example.reqhqezhrtreffh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.reqhqezhrtreffh.saxrssreader.RssFeed;
import com.example.reqhqezhrtreffh.saxrssreader.RssItem;
import com.example.reqhqezhrtreffh.saxrssreader.RssReader;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Display display ;
    static int mposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = getWindowManager().getDefaultDisplay();
        // TODO: Flux rss
        String url = "http://www.bfmtv.com/rss/info/flux-rss/flux-toutes-les-actualites/";

        // start AsyncTask
        new rssReaderTask().execute(url);
    }

    public Display getDisplay() {
        return display;
    }


    public class rssReaderTask extends AsyncTask<String, Void, ArrayList<RssItem>> {
        private WebView mWebView;
        final ArrayList<String> link = new ArrayList<>();
        protected ArrayList<RssItem> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                RssFeed feed = RssReader.read(url);
                Log.i("RSS Reader", "fonctionne");
                return feed.getRssItems();
            } catch (Exception ex) {
                Log.i("RSS Reader", "fonctionne pas");
                return null;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<RssItem> result) {
            final ListView liste1 = (ListView) findViewById(R.id.listView2);
            final ArrayList<String> titre = new ArrayList<String>();
            ArrayList<String> contenu = new ArrayList<String>();
            ArrayList<String> date = new ArrayList<String>();
            ArrayList<String> image = new ArrayList<String>();
            final ArrayList<Boolean> vu = new ArrayList<Boolean>();
            final MyAdapter aa = new MyAdapter(MainActivity.this, contenu,titre,date,image,MainActivity.this.getDisplay(),vu);
            //Format de la date
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                for(int i=0;i<result.size();i++){
                    //Séparation du contenu
                    String[] parts = result.get(i).getDescription().split("<br/><br/>");
                    String[] parts2 = parts[0].split("\\.");
                    contenu.add(i,parts2[0] + ".");
                    //Récupération du lien de l'image
                    String[] separation = parts[1].split("src=\"");
                    String[] lienimage = separation[1].split("\"/>");
                    image.add(i, new String(lienimage[0]));
                    //Récupération de titre
                    titre.add(i,result.get(i).getTitle());
                    //Convertion de l'objet en date en string
                    String StringDate = df.format(result.get(i).getPubDate());
                    date.add(i,StringDate);
                    link.add(i,result.get(i).getLink());
                    vu.add(i,false);
                }
            } catch (Exception ex) {
                Log.i("onPostExecute", ""+ex);
            }
            // TODO: Adapter personnalisé
            liste1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, final int  position, long id) {
                    vu.set(position,true);
                    aa.notifyDataSetChanged();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(MainActivity.this, WebClient.class);
                            myIntent.putExtra("url",link.get(position));
                            MainActivity.this.startActivity(myIntent);
                        }
                    }).setNegativeButton("non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.setMessage("Voulez-vous ouvrir le lien ?");
                    builder.setTitle("Confirmation");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            liste1.setAdapter(aa);
        }
    }

}
