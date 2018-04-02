package com.example.reqhqezhrtreffh;

/**
 * Created by thema on 02/04/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static com.example.reqhqezhrtreffh.MainActivity.mposition;

public class MyAdapter extends BaseAdapter {

    ArrayList<Boolean> vul;
    ArrayList<String> title;
    ArrayList<String> description;
    ArrayList<String> date;
    ArrayList<String> url;
    Display display;
    Activity mContext;

    //constructor
    public MyAdapter(Activity mContext, ArrayList<String> title, ArrayList<String> description,ArrayList<String> date,ArrayList<String> url,Display display,ArrayList<Boolean> vu) {
        this.mContext = mContext;
        this.title = title;
        this.description = description;
        this.date = date;
        this.url=url;
        this.display=display;
        this.vul=vu;
    }

    public int getCount() {
        return title.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View arg1, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row, viewGroup, false);

        TextView addresstext = (TextView) row.findViewById(R.id.textView1);
        TextView bodytext = (TextView) row.findViewById(R.id.textView2);
        TextView dateview = (TextView) row.findViewById(R.id.textView3);
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        //Ajout de l'image
        Point size = new Point();
        display.getSize(size);
        Picasso.get().load(url.get(position)).resize(size.x,size.y/2).into(image);
        bodytext.setText(title.get(position));
        addresstext.setText(description.get(position));
        dateview.setText(date.get(position));
        if (vul.get(position) == true) {
            bodytext.setTextColor(Color.RED);
        }
        return row;
    }
}