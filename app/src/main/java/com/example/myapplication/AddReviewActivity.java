package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.suke.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;

public class AddReviewActivity extends AppCompatActivity {

    //fields
    private String rating = "0";
    private boolean parkingValue = false;
    private boolean accessibilityValue = false;
    private boolean toiletValue = false;
    private boolean serviceValue = false;
    private String extraInfo = "";
    private String chosenPlaceId = "";
    private String reviewsCounter = "temp";

    String TAG = "AddReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database
        setContentView(R.layout.activity_add_review);
        getSupportActionBar().hide(); //creates full screen
        // takes the chosen place's id from ResultACtivity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                chosenPlaceId = null;
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId");
                reviewsCounter = extras.getString("reviewsCounter");
            }
        } else {
            chosenPlaceId = (String) savedInstanceState.getSerializable("chosenPlaceId");
        }
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        TextView parking_t = findViewById(R.id.parking_text);
        TextView accessibility_t = findViewById(R.id.accessibility_t);
        TextView toilet_t = findViewById(R.id.toilet_text);
        TextView service_t = findViewById(R.id.service_text);
        ImageView parking_imageX = findViewById(R.id.parking_imageX);
        ImageView parking_imageV = findViewById(R.id.parking_imageV);
        parking_imageX.setImageResource(R.drawable.x1);
        parking_imageV.setImageResource(R.drawable.v1);
        parking_imageV.setVisibility(View.INVISIBLE);

        ImageView accessibility_imageV = findViewById(R.id.accessibility_imageV);
        accessibility_imageV.setImageResource(R.drawable.v2);
        ImageView accessibility_imageX = findViewById(R.id.accessibility_imageX);
        accessibility_imageX.setImageResource(R.drawable.x2);
        accessibility_imageV.setVisibility(View.INVISIBLE);
                
        ImageView toilet_imageV = findViewById(R.id.wc_imageV);
        toilet_imageV.setImageResource(R.drawable.v3);
        ImageView toilet_imageX = findViewById(R.id.wc_imageX);
        toilet_imageX.setImageResource(R.drawable.x3);
       toilet_imageV.setVisibility(View.INVISIBLE);
                
        ImageView service_imageV = findViewById(R.id.service_imageV);
        service_imageV.setImageResource(R.drawable.v4);
        ImageView service_imageX = findViewById(R.id.service_imageX);
        service_imageX.setImageResource(R.drawable.x4);
        service_imageV.setVisibility(View.INVISIBLE);
                
        //creates a map of the review fields
        Map<String, Object> reviewsMap = new HashMap<>();

        //takes user input ("extra details")
        EditText extra_s = findViewById(R.id.extra_s);

        Intent toResult = new Intent(this, ResultActivity.class);
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        //takes the review and saves in on the database
        Button saveButton;
        saveButton = findViewById(R.id.save_button);//get the id for button


        //parking switch
        com.suke.widget.SwitchButton parking_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.parking_b);

        parking_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                parkingValue = isChecked;
                if (isChecked) {
                    fadeOut(parking_imageV, parking_imageX);
                    fadeIn(parking_imageX, parking_imageV);


                   // parking_t.setBackground(greenWhite());
                 //   parking_image.setImageResource(R.drawable.v1);
                } else {
                  //  parking_t.setBackground(redWhite());
                    fadeIn(parking_imageV, parking_imageX);
                    fadeOut(parking_imageX, parking_imageV);
                }
            }
        });

        //accessibility switch
        com.suke.widget.SwitchButton accessibility_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.accessibility_b);

        accessibility_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                accessibilityValue = isChecked;
                if (isChecked) {
                    fadeOut(accessibility_imageV,accessibility_imageX);
                    fadeIn(accessibility_imageX, accessibility_imageV);
                } else {
                    fadeIn(accessibility_imageV,accessibility_imageX);
                    fadeOut(accessibility_imageX, accessibility_imageV);
                }
            }
        });

        com.suke.widget.SwitchButton toilet_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.toilet_b);

        toilet_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                toiletValue = isChecked;
                if (isChecked) {
                    fadeOut(toilet_imageV,toilet_imageX);
                    fadeIn(toilet_imageX, toilet_imageV);
                } else {
                    fadeIn(toilet_imageV,toilet_imageX);
                    fadeOut(toilet_imageX, toilet_imageV);
                }
            }
        });

        com.suke.widget.SwitchButton service_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.service_b);

        service_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                serviceValue = isChecked;
                if (isChecked) {
                    fadeOut(service_imageV,service_imageX);
                    fadeIn(service_imageX, service_imageV);
                } else {
                    fadeIn(service_imageV,service_imageX);
                    fadeOut(service_imageX, service_imageV);
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss", Locale.getDefault());;
                String currentDateandTime = sdf.format(new Date());
                extraInfo = extra_s.getText().toString();
                reviewsMap.put("rating",rating);
                reviewsMap.put("parking",parkingValue);
                reviewsMap.put("accessibility",accessibilityValue);
                reviewsMap.put("toilet",toiletValue);
                reviewsMap.put("service",serviceValue);
                reviewsMap.put("extraInfo",extraInfo);
                reviewsMap.put("time",currentDateandTime);
                reviewsMap.put("id", reviewsCounter);

                db.collection("places").document(chosenPlaceId).collection("reviews").document(reviewsCounter).
                        set(reviewsMap, SetOptions.merge());
                Toast.makeText(AddReviewActivity.this, AddReviewActivity.this.getString(R.string.thanks_for_review), Toast.LENGTH_LONG).show();
                toResult.putExtra("chosenPlaceId", chosenPlaceId);
                toResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toResult);


            }
        });


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        rating = "2";
                        break;
                    case SmileRating.GOOD:
                        rating = "4";
                        break;
                    case SmileRating.GREAT:
                        rating = "5";
                        break;
                    case SmileRating.OKAY:
                        rating = "3";
                        break;
                    case SmileRating.TERRIBLE:
                        rating = "1";
                        break;
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(AddReviewActivity.this.getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton((AddReviewActivity.this.getString(R.string.yes)), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddReviewActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton((AddReviewActivity.this.getString(R.string.no)), null)
                .show();
    }


    public GradientDrawable redWhite() {

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{ContextCompat.getColor(this,R.color.redLight),
                        ContextCompat.getColor(this, R.color.quantum_white_100)
                });
        return gradientDrawable;
    }



    public void fadeIn(ImageView img1, ImageView img2) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(250);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img2.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        img2.startAnimation(fadeIn);
        img1.setVisibility(View.GONE);
    }

    public void fadeOut(ImageView img1, ImageView img2) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(250);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        img2.startAnimation(fadeOut);
        img1.setVisibility(View.VISIBLE);
    }


    public GradientDrawable greenWhite() {

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{ContextCompat.getColor(this,R.color.greenLight),
                        ContextCompat.getColor(this, R.color.quantum_white_100)
                });
        return gradientDrawable;
    }



}
