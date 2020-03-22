package com.example.myapplication;


import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] date;
    private final Integer[] imgParking;
    private final Integer[] imgAccessibility;
    private final Integer[] imgToilet;
    private final Integer[] imgService;

    public MyListAdapter(Activity context, String[] maintitle, String[] date, Integer[] imgParking, Integer[] imgAccessibility, Integer[] imgToilet, Integer[] imgService) {
        super(context, R.layout.review_list_item, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.imgParking=imgParking;
        this.imgAccessibility=imgAccessibility;
        this.imgToilet=imgToilet;
        this.imgService=imgService;
        this.date = date;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.review_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView dateText = (TextView) rowView.findViewById(R.id.review_date);
        ImageView imageParking = (ImageView) rowView.findViewById(R.id.isVX1);
        ImageView imageAccessibility = (ImageView) rowView.findViewById(R.id.isVX2);
        ImageView imageToilet = (ImageView) rowView.findViewById(R.id.isVX3);
        ImageView imageService = (ImageView) rowView.findViewById(R.id.isVX4);



        titleText.setText(maintitle[position]);
        dateText.setText(date[position]);
        imageParking.setImageResource(imgParking[position]);
        imageAccessibility.setImageResource(imgAccessibility[position]);
        imageToilet.setImageResource(imgToilet[position]);
        imageService.setImageResource(imgService[position]);

        return rowView;

    }
}