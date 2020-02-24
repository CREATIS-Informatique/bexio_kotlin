package ch.creatis.bexio.Next

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_projets_next_saisie_temps.*

class ProjetsActivityNextSaisieTemps : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next_saisie_temps)



        val spinner = findViewById<Spinner>(R.id.spinnerActivities)
        var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")



        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list_of_items)


            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    activitiesSelect.text = list_of_items[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {



                }



            }



        }



    }



}
