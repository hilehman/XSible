package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImagesGalleryActivity : AppCompatActivity() {

    internal var chosenPlaceId: String? = null
    internal var db = FirebaseFirestore.getInstance()  //gets an instance of FireStore database
    private val docRef: DocumentReference? = null
    internal var storage = FirebaseStorage.getInstance()
    internal var storageRef = storage.reference
    internal var pathReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_gallery)
        val picture = findViewById<View>(R.id.pic_from_storage) as ImageView
        val db = FirebaseFirestore.getInstance()  //gets an instance of FireStore database
        supportActionBar!!.hide() //creates full screen
        // takes the chosen place's id from ResultACtivity
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                chosenPlaceId = null
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId")
                pathReference = storageRef.child(chosenPlaceId!!)
            }
        } else {
            chosenPlaceId = savedInstanceState.getSerializable("chosenPlaceId") as String?
        }
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        db.collection("places").document(chosenPlaceId!!).collection("pictures").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    // creates "new review" button
                    if (queryDocumentSnapshots.isEmpty) {
                        Toast.makeText(this@ImagesGalleryActivity, "No pictures yet", Toast.LENGTH_SHORT).show()
                    } else {
                        // puts every document on a map that goes into a list
                        for (document in queryDocumentSnapshots) {
                            //go to reference in the path that is saved in the DB
                            val islandRef = storageRef.child((document.get("pic") as String?)!!)
                            val ONE_MEGABYTE = (1024 * 1024).toLong()
                            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                                val bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                picture.setImageBitmap(bitmapPicture)
                            }.addOnFailureListener {
                                // Handle any errors
                            }

                        }
                    }
                }

    }


}
