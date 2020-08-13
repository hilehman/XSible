//package com.example.myapplication
//
//import android.content.Context
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.CheckedTextView
//
//import com.google.android.libraries.places.api.model.Place
//
//import java.util.ArrayList
//import java.util.Arrays
//
//class PlacesFieldSelector @JvmOverloads constructor(validFields: List<Place.Field> = Arrays.asList<Field>(*Place.Field.values())) {
//
//    private val placeFields: MutableList<PlaceField>
//
//    /**
//     * Returns all [Place.Field] that are selectable.
//     */
//    val allFields: List<Place.Field>
//        get() {
//            val list = ArrayList<Place.Field>()
//            for (placeField in placeFields) {
//                list.add(placeField.field)
//            }
//
//            return list
//        }
//
//    /**
//     * Returns all [Place.Field] values the user selected.
//     */
//    val selectedFields: List<Place.Field>
//        get() {
//            val selectedList = ArrayList<Place.Field>()
//            for (placeField in placeFields) {
//                if (placeField.checked) {
//                    selectedList.add(placeField.field)
//                }
//            }
//
//            return selectedList
//        }
//
//    /**
//     * Returns a String representation of all selected [Place.Field] values. See [ ][.getSelectedFields].
//     */
//    val selectedString: String
//        get() {
//            val builder = StringBuilder()
//            for (field in selectedFields) {
//                builder.append(field).append("\n")
//            }
//
//            return builder.toString()
//        }
//
//    init {
//        placeFields = ArrayList()
//        for (field in validFields) {
//            placeFields.add(PlaceField(field))
//        }
//    }
//
//    //////////////////////////
//    // Helper methods below //
//    //////////////////////////
//
//    private class PlaceField(internal val field: Place.Field) {
//        internal var checked: Boolean = false
//    }
//
//    private class PlaceFieldArrayAdapter(context: Context, placeFields: List<PlaceField>) : ArrayAdapter<PlaceField>(context, android.R.layout.simple_list_item_multiple_choice, placeFields), AdapterView.OnItemClickListener {
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val view = super.getView(position, convertView, parent)
//            val placeField = getItem(position)
//            updateView(view, placeField)
//
//            return view
//        }
//
//        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//            val placeField = getItem(position)
//            placeField!!.checked = !placeField.checked
//            updateView(view, placeField)
//        }
//
//        private fun updateView(view: View, placeField: PlaceField?) {
//            if (view is CheckedTextView) {
//                view.text = placeField!!.field.toString()
//                view.isChecked = placeField.checked
//            }
//        }
//
//
//    }
//}