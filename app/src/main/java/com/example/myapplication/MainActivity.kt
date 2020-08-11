package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

import java.io.Serializable

import com.google.android.libraries.places.api.model.TypeFilter.ESTABLISHMENT

class MainActivity : AppCompatActivity(), Serializable {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //loadLocale();
        // create a full screen window
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        // adapts the image to the size of the display */

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val button: Button
        button = findViewById(R.id.search_button)


        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBZKbr8RFXazrIAw6fD7705MMdcL1idUdA")
        }

        // Create a new Places client instance.
        val fieldSelector = PlacesFieldSelector()
        val searchHint = getString(R.string.search_hint)
        val autocompleteIntent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.allFields)
                .setHint(searchHint).setCountry("il").setTypeFilter(ESTABLISHMENT).build(this)

        button.setOnClickListener { startActivityForResult(autocompleteIntent, 10) }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val toMain = Intent(this, ResultActivity::class.java)
        if (requestCode == 10) {
            if (resultCode == -1) {
                val place = Autocomplete.getPlaceFromIntent(intent!!)
                val chosenPlaceId = place.id
                val toResult = Intent(this, ResultActivity::class.java)
                toResult.putExtra("chosenPlaceId", chosenPlaceId)
                startActivity(toResult)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()

            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            }
        }


    }


}