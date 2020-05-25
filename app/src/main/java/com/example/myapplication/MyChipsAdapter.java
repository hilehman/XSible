package com.example.myapplication;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyChipsAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final String[] chip1Array;
    private final String[] chip2Array;
    private final String[] chip3Array;
    private final String[] chip4Array;
    private final String[] chip5Array;
    private final String[] chip6Array;
    private final String[] chip7Array;
    private final String[] chip8Array;



    public MyChipsAdapter(Activity context, String[] chip1, String[] chip2, String[] chip3, String[] chip4, String[] chip5, String[] chip6, String[] chip7, String[] chip8) {

        super(context, R.layout.review_chips_item);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.chip1Array = chip1;
        this.chip2Array = chip2;
        this.chip3Array = chip3;
        this.chip4Array = chip4;
        this.chip5Array = chip5;
        this.chip6Array = chip6;
        this.chip7Array = chip7;
        this.chip8Array = chip8;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.review_chips_item, null,true);

        TextView chip1 = (TextView)  rowView.findViewById(R.id.chip1);
        TextView chip2 = (TextView)  rowView.findViewById(R.id.chip2);
        TextView chip3 = (TextView)  rowView.findViewById(R.id.chip3);
        TextView chip4 = (TextView)  rowView.findViewById(R.id.chip4);
        TextView chip5 = (TextView)  rowView.findViewById(R.id.chip5);
        TextView chip6 = (TextView)  rowView.findViewById(R.id.chip6);
        TextView chip7 = (TextView)  rowView.findViewById(R.id.chip7);
        TextView chip8 = (TextView)  rowView.findViewById(R.id.chip8);

        chip1.setText(chip1Array[position]);
        chip2.setText(chip2Array[position]);
        chip3.setText(chip3Array[position]);
        chip4.setText(chip4Array[position]);
        chip5.setText(chip5Array[position]);
        chip6.setText(chip6Array[position]);
        chip7.setText(chip7Array[position]);
        chip8.setText(chip8Array[position]);

        return rowView;

    }
}