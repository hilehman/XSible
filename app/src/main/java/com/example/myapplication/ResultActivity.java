package com.example.myapplication;
//

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Downloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;

//

public class ResultActivity extends AppCompatActivity implements Serializable {

    // FIELDS//
    private String chosenPlaceName = "";
    private String chosenPlaceId = "";
    private String chosenPlaceAddress = "";
    private String getChosenPlacePhone = "";
    private String chosenPlaceURL = "";
    private List<Map<String, Object>> reviewsList = new ArrayList<>();
    private ListView list;
    double summedGrade = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database
    private DocumentReference docRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    private TextView parking_grade;
    private TextView accessibility_grade;
    private TextView toilet_grade;
    private TextView service_grade;
    private ProgressBar pbParking;
    private ProgressBar pbAccessibility;
    private ProgressBar pbToilet;
    private ProgressBar pbService;
    private TextView openGoogleMapText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a full screen window
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        // adapts the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        getChosenId(savedInstanceState); //takes id from extra and initiate the field



        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(chosenPlaceId, placeFields);
        final PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

            parking_grade = (TextView) findViewById(R.id.parking_grade);
            accessibility_grade = (TextView) findViewById(R.id.accessibility_grade);
            toilet_grade = (TextView) findViewById(R.id.toilet_grade);
            service_grade = (TextView) findViewById(R.id.service_grade);
            pbParking = (ProgressBar) findViewById(R.id.progressBar_parking);
            pbAccessibility = (ProgressBar) findViewById(R.id.progressBar_accessibility);
            pbToilet = (ProgressBar) findViewById(R.id.progressBar_toilet);
            pbService = (ProgressBar) findViewById(R.id.progressBar_service);
            openGoogleMapText = (TextView) findViewById(R.id.open_google_map_text);
            Place chosenPlace = response.getPlace(); //gets Place object
            chosenPlaceName = chosenPlace.getName();
            chosenPlaceAddress = chosenPlace.getAddress();
            getChosenPlacePhone = chosenPlace.getPhoneNumber();
            Map<String, Object> chosenPlaceMap = new HashMap<>(); //creates map with place's details
            final TextView place_name = (TextView) findViewById(R.id.place_name); //get the id for TextView
            final TextView place_address = (TextView) findViewById(R.id.place_address); //get the id for TextView
            //todo add phone number textview in XML and here
            docRef = db.collection("places").document(chosenPlaceId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) { //if there is already a collection for this places
                            db.collection("places").document(chosenPlaceId).update("reviewsCounter", reviewsList.size()); //updates its reviewsCounter
                            place_name.setText(chosenPlaceName); //displays Place's name
                            place_address.setText(chosenPlaceAddress); //displays Place's address
                            //todo initiaet phone number textview
                        } else { //creates the collection with the different fields
                            chosenPlaceMap.put("id", chosenPlace.getId());
                            chosenPlaceMap.put("name", chosenPlace.getName());
                            chosenPlaceMap.put("address", chosenPlace.getAddress());
                            chosenPlaceMap.put("longitude", String.valueOf(chosenPlace.getLatLng().latitude)); //todo - bug here!
                            chosenPlaceMap.put("latitude", String.valueOf(chosenPlace.getLatLng().latitude));
                            chosenPlaceMap.put("phone", chosenPlace.getPhoneNumber());
                            chosenPlaceMap.put("link", "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlace.getId());
                            chosenPlaceMap.put("reviewsCounter", 0);
                            db.collection("places").document(chosenPlaceId) // creates a document named <placeID> and add it to db
                                    .set(chosenPlaceMap, SetOptions.merge()); //

                            place_name.setText(chosenPlaceName); //displays Place's name
                            place_address.setText(getShortAddress(chosenPlaceAddress)); //displays Place's address

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
        ;
        // creates "open map" button
        final ImageButton open_map_intent = (ImageButton) findViewById(R.id.open_map_buttom);
        Intent toOpenMap = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlaceId));
        open_map_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toOpenMap);
            }
        });


        final TextView no_reviews_yet = (TextView) findViewById(R.id.no_reviews_yet);
        no_reviews_yet.setVisibility(View.VISIBLE);
        // final TextView avgGrade = (TextView) findViewById(R.id.avg_grade);
        // final TextView avgGradeText = (TextView) findViewById(R.id.avg_grade_text);


        //  avgGradeText.setVisibility(View.INVISIBLE);
        //   avgGrade.setVisibility(View.INVISIBLE);


        // gets review from data base into a listView
        db.collection("places").document(chosenPlaceId).collection("reviews").orderBy("id", DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //builds the floating button

                        // creates "new review" button
                        ExtendedFloatingActionButton add_review = (ExtendedFloatingActionButton) findViewById(R.id.add_review_icon_text);
                        add_review.extend(true);
                        //intent to add_review activity
                        Intent toAddReview = new Intent(ResultActivity.this, AddReviewActivity.class);
                        add_review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toAddReview.putExtra("chosenPlaceId", chosenPlaceId);
                                toAddReview.putExtra("reviewsCounter", Integer.toString(reviewsList.size()));
                                //todo does it takes care of back from adding review before posting a review
                                startActivity(toAddReview);
                            }
                        });
                        // creates "add pivcture" button
                        ExtendedFloatingActionButton add_picture = (ExtendedFloatingActionButton) findViewById(R.id.add_picture_icon_text);
                        add_picture.extend(true);

                        add_picture.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View view) {
                                onLaunchCamera();
                           }
                        });

                        // creates "see picture" button
                        ExtendedFloatingActionButton see_picture = (ExtendedFloatingActionButton) findViewById(R.id.open_picture_icon_text);
                        see_picture.extend(true);
                        see_picture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent toImagesGallery = new Intent(ResultActivity.this, ImagesGalleryActivity.class);
                                see_picture.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        toImagesGallery.putExtra("chosenPlaceId", chosenPlaceId);
                                        startActivity(toImagesGallery);
                                    }
                                });


                            }
                        });


                        if (queryDocumentSnapshots.isEmpty()) {


                            no_reviews_yet.setVisibility(View.VISIBLE);
                            no_reviews_yet.bringToFront();
                        } else {
                            no_reviews_yet.setVisibility(View.INVISIBLE);



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
                                String[] chip1 = new String[reviewsList.size()];
                                Double[] grades = new Double[reviewsList.size()];
                                // puts data in the arrays
                                int counterParkingGrade = 0;
                                int counterAccessibilityGrade = 0;
                                int counterToiletGrade = 0;
                                int counterServiceGrade = 0;
                                int counter = 0;
                                int dontCount = 0;
                                for (Map<String, Object> currMap : reviewsList) {
                                    extraInfo[counter] = (String) currMap.get("extraInfo");
                                    chip1[counter]= (String) currMap.get("chip1");
                                    date[counter] = ((String) currMap.get("time")).substring(0, 10);
                                    
                                    
                                    if ((Boolean) currMap.get("parking")){
                                        imgParking[counter] = R.drawable.v1;
                                        counterParkingGrade++;
                                    }
                                    else imgParking[counter] = R.drawable.x1;

                                    if ((Boolean) currMap.get("accessibility")){
                                        imgAccessibility[counter] = R.drawable.v2;
                                        counterAccessibilityGrade++;
                                    }
                                    else imgAccessibility[counter] = R.drawable.x2;

                                    if ((Boolean) currMap.get("toilet")){
                                        imgToilet[counter] = R.drawable.v3;
                                        counterToiletGrade++;
                                    }
                                    else imgToilet[counter] = R.drawable.x3;

                                    if ((Boolean) currMap.get("service")){
                                        imgService[counter] = R.drawable.v4;
                                        counterServiceGrade++;
                                    }
                                    else imgService[counter] = R.drawable.x4;
                                    
                                    Integer currGrade = Integer.valueOf(String.valueOf(currMap.get("rating")));
                                    if (currGrade != 0) summedGrade += currGrade;
                                    else dontCount++;
                                    counter++;
                                }

                                pbParking.setMax(counter);
                                pbAccessibility.setMax(counter);
                                pbToilet.setMax(counter);
                                pbService.setMax(counter);
                                parking_grade.setText(String.valueOf(counterParkingGrade));
                                accessibility_grade.setText(String.valueOf(counterAccessibilityGrade));
                                toilet_grade.setText(String.valueOf(counterToiletGrade));
                                service_grade.setText(String.valueOf(counterServiceGrade));

                                pbParking.setProgress(counterParkingGrade);
                                pbAccessibility.setProgress(counterAccessibilityGrade);
                                pbToilet.setProgress(counterToiletGrade);
                                pbService.setProgress(counterServiceGrade);

                                //calculate grade for current place based on revires (if more than 3)
                                if (reviewsList.size() - dontCount > 3) {
                                    //      avgGradeText.setVisibility(View.VISIBLE);
                                    //     avgGrade.setVisibility(View.VISIBLE);
                                    double grade = summedGrade / (reviewsList.size() - dontCount);
                                    double finalGrade = Math.round(grade * 10) / 10.0;
                                  /*  if (finalGrade % 1 == 0)
                                        avgGradeText.setText(String.valueOf((int)finalGrade));
                                    else avgGradeText.setText(String.valueOf(finalGrade));*/
                                }
                                // uses the adapter to insert data to listView
                                MyListAdapter adapter = new MyListAdapter(ResultActivity.this, extraInfo, date, imgParking, imgAccessibility, imgToilet, imgService, chip1  );
                                list = (ListView) findViewById(R.id.list);
                                list.setAdapter(adapter);

                                list.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem,
                                                         int visibleItemCount, int totalItemCount) {

                                    }

                                    @Override
                                    //takes care of the floating button (shrink and expand)
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        add_review.setTextSize(1, 1);
                                        add_review.shrink(true);
                                        add_picture.setTextSize(1, 1);
                                        add_picture.shrink(true);
                                        see_picture.setTextSize(1, 1);
                                        see_picture.shrink(true);
                                    }
                                });

                            }
                        }
                    }
                });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String imagePath = encodeBitmapAndSaveToFirebase(imageBitmap);
            Map<String, Object> imageMap = new HashMap<>();
            imageMap.put("pic", imagePath);
            db.collection("places").document(chosenPlaceId).collection("pictures").document().set(imageMap);
            progressDialog = getDialogProgressBar().create();
            progressDialog.show();
            try{
                Thread.sleep(1200);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss", Locale.getDefault());;
        String currentDateandTime = sdf.format(new Date());
        StorageReference placeRef = storageRef.child(chosenPlaceId).child(currentDateandTime);
        placeRef.putBytes(data);
        return placeRef.getPath();
    }



    private String getShortAddress(String fullAddress) {
        int secComma = fullAddress.indexOf(',', fullAddress.indexOf(',') + 1);
        if (fullAddress.length() > secComma) fullAddress = fullAddress.substring(0, secComma);
        return fullAddress;
    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }

    void getChosenId(Bundle savedInstanceState) {
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
    }


}













