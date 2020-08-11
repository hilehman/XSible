package com.example.myapplication


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyChipsAdapter(private val context: Activity, private val chip1Array: Array<String>, private val chip2Array: Array<String>, private val chip3Array: Array<String>, private val chip4Array: Array<String>, private val chip5Array: Array<String>, private val chip6Array: Array<String>, private val chip7Array: Array<String>, private val chip8Array: Array<String>)// TODO Auto-generated constructor stub
    : ArrayAdapter<String>(context, R.layout.review_chips_item) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.review_chips_item, null, true)

        val chip1 = rowView.findViewById<View>(R.id.chip1) as TextView
        val chip2 = rowView.findViewById<View>(R.id.chip2) as TextView
        val chip3 = rowView.findViewById<View>(R.id.chip3) as TextView
        val chip4 = rowView.findViewById<View>(R.id.chip4) as TextView
        val chip5 = rowView.findViewById<View>(R.id.chip5) as TextView
        val chip6 = rowView.findViewById<View>(R.id.chip6) as TextView
        val chip7 = rowView.findViewById<View>(R.id.chip7) as TextView
        val chip8 = rowView.findViewById<View>(R.id.chip8) as TextView

        chip1.text = chip1Array[position]
        chip2.text = chip2Array[position]
        chip3.text = chip3Array[position]
        chip4.text = chip4Array[position]
        chip5.text = chip5Array[position]
        chip6.text = chip6Array[position]
        chip7.text = chip7Array[position]
        chip8.text = chip8Array[position]

        return rowView

    }
}