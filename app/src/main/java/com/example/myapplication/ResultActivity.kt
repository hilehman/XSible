package com.example.myapplication

//

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.FileUtils
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Downloader

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Arrays
import java.util.Date
import java.util.HashMap
import java.util.Locale
import com.google.firebase.firestore.Query.Direction.DESCENDING

//

class ResultActivity : AppCompatActivity(), Serializable {

    // FIELDS//
    private var chosenPlaceName: String? = ""
    private var chosenPlaceId: String? = ""
    private var chosenPlaceAddress: String? = ""
    private var getChosenPlacePhone: String? = ""
    private val chosenPlaceURL = ""
    private val reviewsList = ArrayList<Map<String, Any>>()
    private var list: ListView? = null
    private val chipsList: ListView? = null
    internal var summedGrade = 0.0
    internal var db = FirebaseFirestore.getInstance()  //gets an instance of FireStore database
    private var docRef: DocumentReference? = null
    internal var storage = FirebaseStorage.getInstance()
    internal var storageRef = storage.reference
    //internal var builder: AlertDialog.Builder? = null
    internal var builder = AlertDialog.Builder(this)
    internal var progressDialog: AlertDialog
    private var parking_grade: TextView? = null
    private var accessibility_grade: TextView? = null
    private var toilet_grade: TextView? = null
    private var service_grade: TextView? = null
    private var pbParking: ProgressBar? = null
    private var pbAccessibility: ProgressBar? = null
    private var pbToilet: ProgressBar? = null
    private var pbService: ProgressBar? = null
    private var openGoogleMapText: TextView? = null
    private var chip1Text: TextView? = null
    private var chip2Text: TextView? = null
    private var chip3Text: TextView? = null
    private var chip4Text: TextView? = null
    private var chip5Text: TextView? = null
    private var chip6Text: TextView? = null
    private var chip7Text: TextView? = null
    private var chip8Text: TextView? = null
    private var reviewsSum: TextView? = null
    private var reviewsSumCount = ""
    internal var place_name: TextView
    internal var place_address: TextView
    internal var chosenPlace: Place

    val dialogProgressBar: AlertDialog.Builder
        get() {

            if (builder == null) {
                builder = AlertDialog.Builder(this)

                builder!!.setTitle("Loading...")

                val progressBar = ProgressBar(this)
                val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                progressBar.layoutParams = lp
                builder!!.setView(progressBar)
            }
            return builder
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create a full screen window
        setContentView(R.layout.activity_result)
        supportActionBar!!.hide()

        // adapts the image to the size of the display */
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        getChosenId(savedInstanceState) //takes id from extra and initiate the field

        place_name = findViewById<View>(R.id.place_name) as TextView //get the id for TextView
        place_address = findViewById<View>(R.id.place_address) as TextView //get the id for TextView
        parking_grade = findViewById<View>(R.id.parking_grade) as TextView
        accessibility_grade = findViewById<View>(R.id.accessibility_grade) as TextView
        toilet_grade = findViewById<View>(R.id.toilet_grade) as TextView
        service_grade = findViewById<View>(R.id.service_grade) as TextView
        pbParking = findViewById<View>(R.id.progressBar_parking) as ProgressBar
        pbAccessibility = findViewById<View>(R.id.progressBar_accessibility) as ProgressBar
        pbToilet = findViewById<View>(R.id.progressBar_toilet) as ProgressBar
        pbService = findViewById<View>(R.id.progressBar_service) as ProgressBar
        openGoogleMapText = findViewById<View>(R.id.open_google_map_text) as TextView

        reviewsSum = findViewById<View>(R.id.reviews_sum_text) as TextView
        chip1Text = findViewById<View>(R.id.chip1) as TextView
        chip2Text = findViewById<View>(R.id.chip2) as TextView
        chip3Text = findViewById<View>(R.id.chip3) as TextView
        chip4Text = findViewById<View>(R.id.chip4) as TextView
        chip5Text = findViewById<View>(R.id.chip5) as TextView
        chip6Text = findViewById<View>(R.id.chip6) as TextView
        chip7Text = findViewById<View>(R.id.chip7) as TextView
        chip8Text = findViewById<View>(R.id.chip8) as TextView

        chip1Text!!.visibility = View.GONE
        chip2Text!!.visibility = View.GONE
        chip3Text!!.visibility = View.GONE
        chip4Text!!.visibility = View.GONE
        chip5Text!!.visibility = View.GONE
        chip6Text!!.visibility = View.GONE
        chip7Text!!.visibility = View.GONE
        chip8Text!!.visibility = View.GONE
        val chosenPlaceMap = HashMap<String, Any?>() //creates map with place's details
        //TODO check if the change of Any? is correct
        //builds the floating button

        // creates "new review" button
        val add_review = findViewById<View>(R.id.add_review_icon_text) as ExtendedFloatingActionButton
        add_review.extend(true)
        //intent to add_review activity
        val toAddReview = Intent(this@ResultActivity, AddReviewActivity::class.java)
        add_review.setOnClickListener {
            toAddReview.putExtra("chosenPlaceId", chosenPlaceId)
            toAddReview.putExtra("reviewsCounter", Integer.toString(reviewsList.size))
            //todo does it takes care of back from adding review before posting a review
            startActivity(toAddReview)
        }
        // creates "add pivcture" button
        val add_picture = findViewById<View>(R.id.add_picture_icon_text) as ExtendedFloatingActionButton
        add_picture.extend(true)

        add_picture.setOnClickListener { onLaunchCamera() }

        // creates "see picture" button
        val see_picture = findViewById<View>(R.id.open_picture_icon_text) as ExtendedFloatingActionButton
        see_picture.extend(true)
        see_picture.setOnClickListener {
            val toImagesGallery = Intent(this@ResultActivity, ImagesGalleryActivity::class.java)
            see_picture.setOnClickListener {
                toImagesGallery.putExtra("chosenPlaceId", chosenPlaceId)
                startActivity(toImagesGallery)
            }
        }

        // creates "open map" button
        val open_map_intent = findViewById<View>(R.id.open_map_buttom) as ImageButton
        val toOpenMap = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlaceId!!))
        open_map_intent.setOnClickListener { startActivity(toOpenMap) }
        openGoogleMapText!!.setOnClickListener { startActivity(toOpenMap) }

        val placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        val request = FetchPlaceRequest.newInstance(chosenPlaceId!!, placeFields)
        val placesClient = Places.createClient(this)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            chosenPlace = response.place //gets Place object
            chosenPlaceName = chosenPlace.name
            chosenPlaceAddress = chosenPlace.address
            getChosenPlacePhone = chosenPlace.phoneNumber


            //todo add phone number textview in XML and here
            docRef = db.collection("places").document(chosenPlaceId!!)
            docRef!!.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) { //if there is already a collection for this places
                        db.collection("places").document(chosenPlaceId!!).update("reviewsCounter", reviewsList.size) //updates its reviewsCounter
                        place_name.text = chosenPlaceName //displays Place's name
                        place_address.text = chosenPlaceAddress //displays Place's address
                        //todo initiaet phone number textview
                    } else { //creates the collection with the different fields
                        chosenPlaceMap["id"] = chosenPlace.id
                        chosenPlaceMap["name"] = chosenPlace.name
                        chosenPlaceMap["address"] = chosenPlace.address
                        /*                            chosenPlaceMap.put("longitude", String.valueOf(chosenPlace.getLatLng().latitude)); //todo - bug here!
                            chosenPlaceMap.put("latitude", String.valueOf(chosenPlace.getLatLng().latitude));*/
                        chosenPlaceMap["phone"] = chosenPlace.phoneNumber
                        chosenPlaceMap["link"] = "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlace.id!!
                        chosenPlaceMap["reviewsCounter"] = 0
                        db.collection("places").document(chosenPlaceId!!) // creates a document named <placeID> and add it to db
                                .set(chosenPlaceMap, SetOptions.merge()) //

                        place_name.text = chosenPlaceName //displays Place's name
                        place_address.text = getShortAddress(chosenPlaceAddress!!) //displays Place's address

                    }
                } else {
                    Log.d("ResultActivity", "get failed with ", task.exception)
                }
            }

        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                // Handle error with given status code.
                Toast.makeText(this@ResultActivity, "FAILED", Toast.LENGTH_LONG).show()
            }
        }


        val no_reviews_yet = findViewById<View>(R.id.no_reviews_yet) as TextView
        no_reviews_yet.visibility = View.VISIBLE
        // final TextView avgGrade = (TextView) findViewById(R.id.avg_grade);
        // final TextView avgGradeText = (TextView) findViewById(R.id.avg_grade_text);


        //  avgGradeText.setVisibility(View.INVISIBLE);
        //   avgGrade.setVisibility(View.INVISIBLE);


        // gets review from data base into a listView
        db.collection("places").document(chosenPlaceId!!).collection("reviews").orderBy("id", DESCENDING).get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    if (queryDocumentSnapshots.isEmpty) {


                        no_reviews_yet.visibility = View.VISIBLE
                        no_reviews_yet.bringToFront()

                    } else {
                        no_reviews_yet.visibility = View.INVISIBLE


                        // puts every document on a map that goes into a list
                        for (document in queryDocumentSnapshots) {
                            val tempMap = document.data
                            reviewsList.add(tempMap)
                        }

                        if (!reviewsList.isEmpty()) {


                            // creates arrays to hold data
                            val extraInfo = arrayOfNulls<String>(reviewsList.size)
                            val date = arrayOfNulls<String>(reviewsList.size)
                            val imgParking = arrayOfNulls<Int>(reviewsList.size)
                            val imgAccessibility = arrayOfNulls<Int>(reviewsList.size)
                            val imgToilet = arrayOfNulls<Int>(reviewsList.size)
                            val imgService = arrayOfNulls<Int>(reviewsList.size)
                            /*                   String[] chip1 = new String[reviewsList.size()];
                                String[] chip2 = new String[reviewsList.size()];
                                String[] chip3 = new String[reviewsList.size()];
                                String[] chip4 = new String[reviewsList.size()];
                                String[] chip5 = new String[reviewsList.size()];
                                String[] chip6 = new String[reviewsList.size()];
                                String[] chip7 = new String[reviewsList.size()];
                                String[] chip8= new String[reviewsList.size()]; */
                            var chip1Counter = 0
                            var chip2Counter = 0
                            var chip3Counter = 0
                            var chip4Counter = 0
                            var chip5Counter = 0
                            var chip6Counter = 0
                            var chip7Counter = 0
                            var chip8Counter = 0
                            val grades = arrayOfNulls<Double>(reviewsList.size)
                            // puts data in the arrays
                            var counterParkingGrade = 0
                            var counterAccessibilityGrade = 0
                            var counterToiletGrade = 0
                            var counterServiceGrade = 0
                            var counter = 0
                            var dontCount = 0
                            for (currMap in reviewsList) {
                                extraInfo[counter] = currMap["extraInfo"] as String?
                                /*                                    chip1[counter]= (String) currMap.get("chip1");
                                    chip2[counter]= (String) currMap.get("chip2");
                                    chip3[counter]= (String) currMap.get("chip3");
                                    chip4[counter]= (String) currMap.get("chip4");
                                    chip5[counter]= (String) currMap.get("chip5");
                                    chip6[counter]= (String) currMap.get("chip6");
                                    chip7[counter]= (String) currMap.get("chip7");
                                    chip8[counter]= (String) currMap.get("chip8");*/
                                if (currMap.containsKey("chip1")) {
                                    chip1Counter++
                                    chip1Text!!.text = currMap["chip1"] as String?
                                }
                                if (currMap.containsKey("chip2")) {
                                    chip2Counter++
                                    chip2Text!!.text = currMap["chip2"] as String?
                                }
                                if (currMap.containsKey("chip3")) {
                                    chip3Counter++
                                    chip3Text!!.text = currMap["chip3"] as String?
                                }
                                if (currMap.containsKey("chip4")) {
                                    chip4Counter++
                                    chip4Text!!.text = currMap["chip4"] as String?
                                }
                                if (currMap.containsKey("chip5")) {
                                    chip5Counter++
                                    chip5Text!!.text = currMap["chip5"] as String?
                                }
                                if (currMap.containsKey("chip6")) {
                                    chip6Counter++
                                    chip6Text!!.text = currMap["chip6"] as String?
                                }
                                if (currMap.containsKey("chip7")) {
                                    chip7Counter++
                                    chip7Text!!.text = currMap["chip7"] as String?
                                }
                                if (currMap.containsKey("chip8")) {
                                    chip8Counter++
                                    chip8Text!!.text = currMap["chip8"] as String?
                                }

                                date[counter] = (currMap["time"] as String).substring(0, 10)


                                if ((currMap["parking"] as Boolean?)!!) {
                                    imgParking[counter] = R.drawable.v1
                                    counterParkingGrade++
                                } else
                                    imgParking[counter] = R.drawable.x1

                                if ((currMap["accessibility"] as Boolean?)!!) {
                                    imgAccessibility[counter] = R.drawable.v2
                                    counterAccessibilityGrade++
                                } else
                                    imgAccessibility[counter] = R.drawable.x2

                                if ((currMap["toilet"] as Boolean?)!!) {
                                    imgToilet[counter] = R.drawable.v3
                                    counterToiletGrade++
                                } else
                                    imgToilet[counter] = R.drawable.x3

                                if ((currMap["service"] as Boolean?)!!) {
                                    imgService[counter] = R.drawable.v4
                                    counterServiceGrade++
                                } else
                                    imgService[counter] = R.drawable.x4

                                val currGrade = Integer.valueOf(currMap["rating"].toString())
                                if (currGrade != 0)
                                    summedGrade += currGrade.toDouble()
                                else
                                    dontCount++
                                counter++
                            }
                            if (chip1Counter != 0) chip1Text!!.visibility = View.VISIBLE
                            if (chip2Counter != 0) chip2Text!!.visibility = View.VISIBLE
                            if (chip3Counter != 0) chip3Text!!.visibility = View.VISIBLE
                            if (chip4Counter != 0) chip4Text!!.visibility = View.VISIBLE
                            if (chip5Counter != 0) chip5Text!!.visibility = View.VISIBLE
                            if (chip6Counter != 0) chip6Text!!.visibility = View.VISIBLE
                            if (chip7Counter != 0) chip7Text!!.visibility = View.VISIBLE
                            if (chip8Counter != 0) chip8Text!!.visibility = View.VISIBLE


                            reviewsSumCount = "סיכום ביקורת משתמשים  ($counter)"
                            if (reviewsSum != null) reviewsSum!!.text = reviewsSumCount
                            pbParking!!.max = counter
                            pbAccessibility!!.max = counter
                            pbToilet!!.max = counter
                            pbService!!.max = counter
                            parking_grade!!.text = counterParkingGrade.toString()
                            accessibility_grade!!.text = counterAccessibilityGrade.toString()
                            toilet_grade!!.text = counterToiletGrade.toString()
                            service_grade!!.text = counterServiceGrade.toString()

                            pbParking!!.progress = counterParkingGrade
                            pbAccessibility!!.progress = counterAccessibilityGrade
                            pbToilet!!.progress = counterToiletGrade
                            pbService!!.progress = counterServiceGrade


                            //calculate grade for current place based on revires (if more than 3)
                            if (reviewsList.size - dontCount > 3) {
                                //      avgGradeText.setVisibility(View.VISIBLE);
                                //     avgGrade.setVisibility(View.VISIBLE);
                                val grade = summedGrade / (reviewsList.size - dontCount)
                                val finalGrade = Math.round(grade * 10) / 10.0
                                /*  if (finalGrade % 1 == 0)
                                        avgGradeText.setText(String.valueOf((int)finalGrade));
                                    else avgGradeText.setText(String.valueOf(finalGrade));*/
                            }
                            // uses the adapter to insert data to listView
                            val adapter = MyListAdapter(this@ResultActivity, extraInfo, date, imgParking, imgAccessibility, imgToilet, imgService)
                            list = findViewById<View>(R.id.list) as ListView
                            list!!.adapter = adapter

                            /*      // uses the adapter to insert data to listView
                                MyChipsAdapter chipsAdapter = new MyChipsAdapter(ResultActivity.this,chip1, chip2, chip3, chip4, chip5, chip6, chip7, chip8 );
                                chipsList = (ListView) findViewById(R.id.chipsList);
                                chipsList.setAdapter(chipsAdapter);*/





                            list!!.setOnScrollListener(object : AbsListView.OnScrollListener {
                                override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                                      visibleItemCount: Int, totalItemCount: Int) {

                                }

                                override//takes care of the floating button (shrink and expand)
                                fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                                    add_review.setTextSize(1, 1f)
                                    add_review.shrink(true)
                                    add_picture.setTextSize(1, 1f)
                                    add_picture.shrink(true)
                                    see_picture.setTextSize(1, 1f)
                                    see_picture.shrink(true)
                                }
                            })

                        }
                    }
                }


    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap?
            val imagePath = encodeBitmapAndSaveToFirebase(imageBitmap!!)
            val imageMap = HashMap<String, Any>()
            imageMap["pic"] = imagePath
            db.collection("places").document(chosenPlaceId!!).collection("pictures").document().set(imageMap)
            progressDialog = dialogProgressBar.create()
            progressDialog.show()
            try {
                Thread.sleep(1200)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            progressDialog.dismiss()
        }
    }

    fun encodeBitmapAndSaveToFirebase(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val sdf = SimpleDateFormat("dd.MM.yyyy_HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        val placeRef = storageRef.child(chosenPlaceId!!).child(currentDateandTime)
        placeRef.putBytes(data)
        return placeRef.path
    }


    private fun getShortAddress(fullAddress: String): String {
        var fullAddress = fullAddress
        val secComma = fullAddress.indexOf(',', fullAddress.indexOf(',') + 1)
        if (fullAddress.length > secComma) fullAddress = fullAddress.substring(0, secComma)
        return fullAddress
    }

    fun onLaunchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

    }

    internal fun getChosenId(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                chosenPlaceId = null
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId")
            }
        } else {
            chosenPlaceId = savedInstanceState.getSerializable("chosenPlaceId") as String?
        }
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
    }

    companion object {
        private val REQUEST_IMAGE_CAPTURE = 2
    }
}

