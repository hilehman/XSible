package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.suke.widget.SwitchButton;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity implements Serializable {

    String chosenPlaceName = "";
    String chosenPlaceaddress = "";
    String chosenPlaceOpenHours = "";

    //  public Place chosenPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database

        /* create a full screen window */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        /* adapt the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.main_background_light),size.x,size.y,true);

        /* fill the background ImageView with the resized image */
        ImageView iv_background = (ImageView) findViewById(R.id.main_background_light);
        iv_background.setImageBitmap(bmp);








        // takes the chosen place's id from MainACtivity
        String chosenPlaceId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                chosenPlaceId = null;
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId");
            }
        } else {
            chosenPlaceId = (String) savedInstanceState.getSerializable("chosenPlaceId");
        }
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(chosenPlaceId, placeFields);
        final PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place chosenPlace = response.getPlace(); //gets Place object
            chosenPlaceName = chosenPlace.getName();
            chosenPlaceaddress = chosenPlace.getAddress();
            Map<String, Object> chosenPlaceMap = new HashMap<>(); //creates map with place's details
            chosenPlaceMap.put("id", chosenPlace.getId());
            chosenPlaceMap.put("name", chosenPlace.getName());
            chosenPlaceMap.put("address", chosenPlace.getAddress());
            chosenPlaceMap.put("link", "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlace.getId());
            db.collection("places").document(chosenPlaceId) // creates a document named <placeID> and add it to db
                    .set(chosenPlaceMap, SetOptions.merge()); //
            final TextView place_name = (TextView) findViewById(R.id.place_name); //get the id for TextView
            final TextView place_address = (TextView) findViewById(R.id.place_address); //get the id for TextView
            place_name.setText(chosenPlaceName); //displays Place's name
            place_address.setText(chosenPlaceaddress); //displays Place's address
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Toast.makeText(ResultActivity.this, "FAILED", Toast.LENGTH_LONG).show();
            }
        });

        setContentView(R.layout.activity_result); //set the layout
        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);

        final Button add_review_intent = (Button) findViewById(R.id.add_review_intent);
        Intent toAddReview = new Intent(this, AddReviewActivity.class);
        toAddReview.putExtra("chosenPlaceId", chosenPlaceId);

        add_review_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(toAddReview);
            }
        });

        final TextView no_reviews_yet = (TextView) findViewById(R.id.no_reviews_yet);

        db.collection("places").document(chosenPlaceId).collection("reviews").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            no_reviews_yet.setText("אין עדיין ביקורות זמינות. \n הנה הזדמנות להתחיל :) ");
                            no_reviews_yet.setVisibility(View.VISIBLE);

                        }
                        else{
                            no_reviews_yet.setVisibility(View.GONE);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResultActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }






/*




        final EditText enterOpinion = (EditText) findViewById(R.id.enter_opinion);//get the id for edit text
        Button saveButton;
        saveButton = findViewById(R.id.save_b);//get the id for button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enterOpinion.getText().toString() != null)//check whether the entered text is not null
                {
                    tempMap.put("opinion", enterOpinion.getText().toString());
                    db.collection("places").document(chosenPlaceId).set(tempMap, SetOptions.merge());
                    Toast.makeText(ResultActivity.this, "SAVED", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ResultActivity.this, "FAILED", Toast.LENGTH_LONG).show();
                }
            }
        });

*/







    }



