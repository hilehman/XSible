package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.OrderBy;
import com.google.type.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class ResultActivity extends AppCompatActivity implements Serializable {

    // FIELDS//
    private String chosenPlaceName = "";
    private String chosenPlaceAddress = "";
    private String chosenPlaceURL = "";
    private List<Map<String, Object>> reviewsList = new ArrayList<>();;
    private ListView list;
    double summedGrade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database

        // create a full screen window
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        // adapts the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.main_background_light), size.x, size.y, true);

        // fills the background ImageView with the resized image
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

        // takes place's details and insert it to the database
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(chosenPlaceId, placeFields);
        final PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place chosenPlace = response.getPlace(); //gets Place object
            chosenPlaceName = chosenPlace.getName();
            chosenPlaceAddress = chosenPlace.getAddress();
            Map<String, Object> chosenPlaceMap = new HashMap<>(); //creates map with place's details
            final TextView place_name = (TextView) findViewById(R.id.place_name); //get the id for TextView
            final TextView place_address = (TextView) findViewById(R.id.place_address); //get the id for TextView
            DocumentReference docRef = db.collection("places").document(chosenPlaceId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) { //if there is already a collection for this places
                            db.collection("places").document(chosenPlaceId).update("reviewsCounter", reviewsList.size()); //updates its reviewsCounter
                            place_name.setText(chosenPlaceName); //displays Place's name
                            place_address.setText(chosenPlaceAddress); //displays Place's address
                        } else { //creates the collection with the different fields
                            chosenPlaceMap.put("id", chosenPlace.getId());
                            chosenPlaceMap.put("name", chosenPlace.getName());
                            chosenPlaceMap.put("address", chosenPlace.getAddress());
                            chosenPlaceMap.put("link", "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlace.getId());
                            chosenPlaceMap.put("reviewsCounter", 0);
                            db.collection("places").document(chosenPlaceId) // creates a document named <placeID> and add it to db
                                    .set(chosenPlaceMap, SetOptions.merge()); //

                            place_name.setText(chosenPlaceName); //displays Place's name
                            place_address.setText(chosenPlaceAddress); //displays Place's address

                        }
                    } else {
                        Log.d("ResultActivity", "get failed with ", task.getException());
                    }
                }
            });

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Toast.makeText(ResultActivity.this, "FAILED", Toast.LENGTH_LONG).show();
            }
        });

        setContentView(R.layout.activity_result); //set the layout
        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);


        // creates "open map" button
        final Button open_map_intent = (Button) findViewById(R.id.open_map_intent);
        Intent toOpenMap = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlaceId));
        open_map_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toOpenMap);
            }
        });

        final TextView no_reviews_yet = (TextView) findViewById(R.id.no_reviews_yet);
        TextView avgGradeText = (TextView) findViewById(R.id.avg_grade_text);
        // gets review from data base into a listView
        db.collection("places").document(chosenPlaceId).collection("reviews").orderBy("id", DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            no_reviews_yet.setText("אין עדיין ביקורות זמינות. \n הנה הזדמנות להתחיל :) ");
                        } else {
                            no_reviews_yet.setVisibility(View.GONE);

                            // puts every document on a map that goes into a list
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Map<String, Object> tempMap = document.getData();
                                reviewsList.add(tempMap);
                            }

                            if (!reviewsList.isEmpty()) {

                                // creates arrays to hold data
                                String[] extraInfo = new String[reviewsList.size()];
                                String[] date = new String[reviewsList.size()];
                                Integer[] imgParking = new Integer[reviewsList.size()];
                                Integer[] imgAccessibility = new Integer[reviewsList.size()];
                                Integer[] imgToilet = new Integer[reviewsList.size()];
                                Integer[] imgService = new Integer[reviewsList.size()];
                                Double[] grades = new Double[reviewsList.size()];
                                // puts data in the arrays
                                int counter = 0;
                                int dontCount = 0;
                                for (Map<String, Object> currMap : reviewsList) {
                                    extraInfo[counter] = (String) currMap.get("extraInfo");
                                    date[counter] = ((String) currMap.get("time")).substring(0, 10);
                                    imgParking[counter] = (Boolean) currMap.get("parking") ? R.drawable.v : R.drawable.x;
                                    imgAccessibility[counter] = (Boolean) currMap.get("accessibility") ? R.drawable.v : R.drawable.x;
                                    imgToilet[counter] = (Boolean) currMap.get("toilet") ? R.drawable.v : R.drawable.x;
                                    imgService[counter] = (Boolean) currMap.get("service") ? R.drawable.v : R.drawable.x;
                                    Integer currGrade = Integer.valueOf(String.valueOf(currMap.get("rating")));
                                    if (currGrade != 0) summedGrade += currGrade;
                                    else dontCount++;
                                    counter++;
                                }
                                if (reviewsList.size() - dontCount > 0) {
                                    double grade = summedGrade / (reviewsList.size() - dontCount);
                                    double finalGrade = Math.round(grade * 10) / 10.0;
                                    if (finalGrade % 1 == 0)
                                        avgGradeText.setText("ציון נגישות ממוצע: 5 / "+String.valueOf((int)finalGrade));
                                    else avgGradeText.setText("ציון נגישות ממוצע: 5 / "+String.valueOf(finalGrade));

                                }
                                // uses the adapter to insert data to listView
                                MyListAdapter adapter = new MyListAdapter(ResultActivity.this, extraInfo, date, imgParking, imgAccessibility, imgToilet, imgService);
                                list = (ListView) findViewById(R.id.list);
                                list.setAdapter(adapter);
                            }
                        }
                    }
                });
        // creates "new review" button
        final Button add_review_intent = (Button) findViewById(R.id.add_review_intent);
        Intent toAddReview = new Intent(this, AddReviewActivity.class);
        add_review_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddReview.putExtra("chosenPlaceId", chosenPlaceId);
                toAddReview.putExtra("reviewsCounter", Integer.toString(reviewsList.size()));
                startActivity(toAddReview);
            }
        });
    }
}


