package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.google.android.libraries.places.api.Places
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hsalf.smilerating.BaseRating
import com.hsalf.smilerating.SmileRating
import com.suke.widget.SwitchButton

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class AddReviewActivity : AppCompatActivity() {

    //fields
    private var rating = "0"
    private var parkingValue = false
    private var accessibilityValue = false
    private var toiletValue = false
    private var serviceValue = false
    private var extraInfo = ""
    private var chosenPlaceId: String? = ""
    private var reviewsCounter: String? = "temp"
    private var chipGroup: ChipGroup? = null
    internal var parkingFrame: FrameLayout
    internal var accessibleFrame: FrameLayout
    internal var toiletFrame: FrameLayout
    internal var serviceFrame: FrameLayout



    internal var TAG = "AddReviewActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()  //gets an instance of FireStore database
        setContentView(R.layout.activity_add_review)
        supportActionBar!!.hide() //creates full screen
        // takes the chosen place's id from ResultACtivity
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                chosenPlaceId = null
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId")
                reviewsCounter = extras.getString("reviewsCounter")
            }
        } else {
            chosenPlaceId = savedInstanceState.getSerializable("chosenPlaceId") as String?
            reviewsCounter = savedInstanceState.getSerializable("reviewsCounter") as String?
        }
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val parking_t = findViewById<TextView>(R.id.parking_text)
        val accessibility_t = findViewById<TextView>(R.id.accessibility_t)
        val toilet_t = findViewById<TextView>(R.id.toilet_text)
        val service_t = findViewById<TextView>(R.id.service_text)

        parkingFrame = findViewById(R.id.parking_frame)
        accessibleFrame = findViewById(R.id.accessible_frame)
        toiletFrame = findViewById(R.id.toilet_frame)
        serviceFrame = findViewById(R.id.service_frame)


        val parking_imageX = findViewById<ImageView>(R.id.parking_imageX)
        val parking_imageV = findViewById<ImageView>(R.id.parking_imageV)




        parking_imageX.setImageResource(R.drawable.x1)
        parking_imageV.setImageResource(R.drawable.v1)
        parking_imageV.visibility = View.INVISIBLE


        val accessibility_imageV = findViewById<ImageView>(R.id.accessibility_imageV)
        accessibility_imageV.setImageResource(R.drawable.v2)
        val accessibility_imageX = findViewById<ImageView>(R.id.accessibility_imageX)
        accessibility_imageX.setImageResource(R.drawable.x2)
        accessibility_imageV.visibility = View.INVISIBLE

        val toilet_imageV = findViewById<ImageView>(R.id.wc_imageV)
        toilet_imageV.setImageResource(R.drawable.v3)
        val toilet_imageX = findViewById<ImageView>(R.id.wc_imageX)
        toilet_imageX.setImageResource(R.drawable.x3)
        toilet_imageV.visibility = View.INVISIBLE

        val service_imageV = findViewById<ImageView>(R.id.service_imageV)
        service_imageV.setImageResource(R.drawable.v4)
        val service_imageX = findViewById<ImageView>(R.id.service_imageX)
        service_imageX.setImageResource(R.drawable.x4)
        service_imageV.visibility = View.INVISIBLE

        chipGroup = this.findViewById<View>(R.id.chipGroup) as ChipGroup

        //creates a map of the review fields
        val reviewsMap = HashMap<String, Any?>()//TODO check if the change to - Any? - is correct
        val chipsMap = HashMap<String, Any>()

        //takes user input ("extra details")
        val extra_s = findViewById<EditText>(R.id.extra_s)

        val toResult = Intent(this, ResultActivity::class.java)
        val smileRating = findViewById<View>(R.id.smile_rating) as SmileRating
        //takes the review and saves in on the database
        val saveButton: Button
        saveButton = findViewById(R.id.save_button)//get the id for button

        val chip1 = addChip("דרך נגישה מהחנייה", chipGroup)
        val chip2 = addChip("מספיק מקומות חנייה", chipGroup)
        val chip3 = addChip("ניתן להכניס כלב שירות", chipGroup)
        val chip4 = addChip("קיים דלפק מונמך", chipGroup)
        val chip5 = addChip("שולחנות מתאימים", chipGroup)
        val chip6 = addChip("תאורה מאפשרת שיח", chipGroup)
        val chip7 = addChip("עובדים סבלניים", chipGroup)
        val chip8 = addChip("קדימות בתור בהצגת תעודה", chipGroup)

        var updatedCounter = Integer.parseInt(reviewsCounter!!)
        updatedCounter++
        reviewsCounter = updatedCounter.toString()
        //parking switch
        val parking_b = findViewById<View>(R.id.parking_b) as com.suke.widget.SwitchButton
        parking_b.setOnCheckedChangeListener { view, isChecked ->
            parkingValue = isChecked
            if (isChecked) {
                fadeOut(parking_imageV, parking_imageX)
                fadeIn(parking_imageX, parking_imageV)
                chipGroup!!.addView(chip1, chipGroup!!.childCount - 1)
                chipGroup!!.addView(chip2, chipGroup!!.childCount - 1)
            } else {
                fadeIn(parking_imageV, parking_imageX)
                fadeOut(parking_imageX, parking_imageV)
                chipGroup!!.removeView(chip1)
                chipGroup!!.removeView(chip2)
            }
        }

        //accessibility switch
        val accessibility_b = findViewById<View>(R.id.accessibility_b) as com.suke.widget.SwitchButton

        accessibility_b.setOnCheckedChangeListener { view, isChecked ->
            accessibilityValue = isChecked
            if (isChecked) {
                fadeOut(accessibility_imageV, accessibility_imageX)
                fadeIn(accessibility_imageX, accessibility_imageV)
                chipGroup!!.addView(chip3, chipGroup!!.childCount - 1)
                chipGroup!!.addView(chip4, chipGroup!!.childCount - 1)
                chipGroup!!.addView(chip5, chipGroup!!.childCount - 1)
                chipGroup!!.addView(chip6, chipGroup!!.childCount - 1)

            } else {
                fadeIn(accessibility_imageV, accessibility_imageX)
                fadeOut(accessibility_imageX, accessibility_imageV)
                chipGroup!!.removeView(chip3)
                chipGroup!!.removeView(chip4)
                chipGroup!!.removeView(chip5)
                chipGroup!!.removeView(chip6)

            }
        }

        val toilet_b = findViewById<View>(R.id.toilet_b) as com.suke.widget.SwitchButton

        toilet_b.setOnCheckedChangeListener { view, isChecked ->
            toiletValue = isChecked
            if (isChecked) {
                fadeOut(toilet_imageV, toilet_imageX)
                fadeIn(toilet_imageX, toilet_imageV)
            } else {
                fadeIn(toilet_imageV, toilet_imageX)
                fadeOut(toilet_imageX, toilet_imageV)
            }
        }

        val service_b = findViewById<View>(R.id.service_b) as com.suke.widget.SwitchButton

        service_b.setOnCheckedChangeListener { view, isChecked ->
            serviceValue = isChecked
            if (isChecked) {
                fadeOut(service_imageV, service_imageX)
                fadeIn(service_imageX, service_imageV)
                chipGroup!!.addView(chip7, chipGroup!!.childCount - 1)
                chipGroup!!.addView(chip8, chipGroup!!.childCount - 1)
            } else {
                fadeIn(service_imageV, service_imageX)
                fadeOut(service_imageX, service_imageV)
                chipGroup!!.removeView(chip7)
                chipGroup!!.removeView(chip8)

            }
        }


        saveButton.setOnClickListener {
            val sdf = SimpleDateFormat("dd.MM.yyyy_HH:mm:ss", Locale.getDefault())
            val currentDateandTime = sdf.format(Date())
            extraInfo = extra_s.text.toString()
            reviewsMap["rating"] = rating
            reviewsMap["parking"] = parkingValue
            reviewsMap["accessibility"] = accessibilityValue
            reviewsMap["toilet"] = toiletValue
            reviewsMap["service"] = serviceValue
            reviewsMap["extraInfo"] = extraInfo
            reviewsMap["time"] = currentDateandTime
            reviewsMap["id"] = reviewsCounter
            if (chip1.isChecked) chipsMap["chip1"] = chip1.text
            if (chip2.isChecked) chipsMap["chip2"] = chip2.text
            if (chip3.isChecked) chipsMap["chip3"] = chip3.text
            if (chip4.isChecked) chipsMap["chip4"] = chip4.text
            if (chip5.isChecked) chipsMap["chip5"] = chip5.text
            if (chip6.isChecked) chipsMap["chip6"] = chip6.text
            if (chip7.isChecked) chipsMap["chip7"] = chip7.text
            if (chip8.isChecked) chipsMap["chip8"] = chip8.text
            db.collection("places").document(chosenPlaceId!!).collection("reviews").document(reviewsCounter!!).set(reviewsMap, SetOptions.merge())
            if (!chipsMap.isEmpty())
                db.collection("places").document(chosenPlaceId!!).collection("reviews").document(reviewsCounter!!).set(chipsMap, SetOptions.merge())
            Toast.makeText(this@AddReviewActivity, this@AddReviewActivity.getString(R.string.thanks_for_review), Toast.LENGTH_LONG).show()
            toResult.putExtra("chosenPlaceId", chosenPlaceId)
            toResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(toResult)
        }


        smileRating.setOnSmileySelectionListener { smiley, reselected ->
            // reselected is false when user selects different smiley that previously selected one
            // true when the same smiley is selected.
            // Except if it first time, then the value will be false.
            when (smiley) {
                SmileRating.BAD -> rating = "2"
                SmileRating.GOOD -> rating = "4"
                SmileRating.GREAT -> rating = "5"
                SmileRating.OKAY -> rating = "3"
                SmileRating.TERRIBLE -> rating = "1"
            }
        }

        parkingFrame.setOnClickListener {
            AlertDialog.Builder(this@AddReviewActivity)
                    .setTitle("חנייה")
                    .setMessage("האם קיימת חנייה נגישה בסמוך? האם הדרך מהחנייה למקום נגישה? האם מספר מקומות החנייה הנגישים מספק?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }

        accessibleFrame.setOnClickListener {
            AlertDialog.Builder(this@AddReviewActivity)
                    .setTitle("כניסה ומרחב")
                    .setMessage("האם מתאפשרת הגעה למקום עם כיסא גלגלים? האם המקום מרווח, נוח, ומאפשר תנועה בקלות?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }

        toiletFrame.setOnClickListener {
            AlertDialog.Builder(this@AddReviewActivity)
                    .setTitle("שירותים")
                    .setMessage("האם קיימים במקום שירותים נגישים? האם ישנן ידיות אחיזה, כיור גבוה, דלת רחבה מספיק?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }

        serviceFrame.setOnClickListener {
            AlertDialog.Builder(this@AddReviewActivity)
                    .setTitle("שירות")
                    .setMessage("האם צוות המקום מסביר פנים ומתנהג באדיבות? נותני השירות מתייחסים בצורה הולמת? ישנה קדימות בתור בהצגת תעודה מתאימה?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setMessage(this@AddReviewActivity.getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(this@AddReviewActivity.getString(R.string.yes)) { dialog, id -> super@AddReviewActivity.onBackPressed() }
                .setNegativeButton(this@AddReviewActivity.getString(R.string.no), null)
                .show()
    }


    /*    public GradientDrawable redWhite() {

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{ContextCompat.getColor(this,R.color.redLight),
                        ContextCompat.getColor(this, R.color.quantum_white_100)
                });
        return gradientDrawable;
    }*/


    fun fadeIn(img1: ImageView, img2: ImageView) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = AccelerateInterpolator()
        fadeIn.duration = 250
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                img2.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })

        img2.startAnimation(fadeIn)
        img1.visibility = View.GONE
    }

    fun fadeOut(img1: ImageView, img2: ImageView) {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 250
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                img2.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        img2.startAnimation(fadeOut)
        img1.visibility = View.VISIBLE
    }

    /*
    public GradientDrawable greenWhite() {

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{ContextCompat.getColor(this,R.color.greenLight),
                        ContextCompat.getColor(this, R.color.quantum_white_100)
                });
        return gradientDrawable;
    }*/

    private fun addChip(text: String, pChipGroup: ChipGroup?): Chip {
        val chip = Chip(this)
        chip.text = text
        chip.isCheckable = true
        chip.setTextColor(resources.getColor(R.color.com_facebook_button_background_color))

        chip.setBackgroundColor(resources.getColor(R.color.quantum_googred200))

        /*  chip.setBackgroundColor(Color.parseColor("#7A28A0F3"));
        chip.setOutlineAmbientShadowColor(Color.parseColor("#7A28A0F3"));*/
        return chip
    }


}
