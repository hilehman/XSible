package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class placeDisplayActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(placeDisplayActivity.this, "ברוכים הבאים!", Toast.LENGTH_LONG).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_display);
        PlaceHolder chosenPlace = (PlaceHolder)  getIntent().getSerializableExtra("MyClass");
        Map<String, Object>chosenPlaceMap = new HashMap<>();
        chosenPlaceMap.put("id", chosenPlace.getPlaceId());
        chosenPlaceMap.put("name", chosenPlace.getName());
        chosenPlaceMap.put("address", chosenPlace.getAddress());








//        String id = b.getString("ld");
//        String name = b.getString("name");
//        String address = b.getString("address");
//        String grade = PlaceHolder.placesMap.get(id).grade;
//        TextView textViewName = (TextView) findViewById(R.id.placeName);
//        TextView textViewAddress = (TextView) findViewById(R.id.placeAddress);
//        TextView textViewGrade = (TextView) findViewById(R.id.grade);
//
//        textViewName.setText(name);
//        textViewAddress.setText(address);
//        textViewName.setText(grade);
//
//
//
//        EditText editTextGrade = (EditText) findViewById(R.id.grade);
//            String newGrade = editTextGrade.getEditableText().toString();
//            PlaceHolder.placesMap.get(id).grade = newGrade;



    }






}
