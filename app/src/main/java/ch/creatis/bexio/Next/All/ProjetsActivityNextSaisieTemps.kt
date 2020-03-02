package ch.creatis.bexio.Next.All

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ch.creatis.bexio.Next.All.TabTime.MyPagerAdapter
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_projets_next_saisie_temps.*

class ProjetsActivityNextSaisieTemps : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next_saisie_temps)



        // --------------- TAB

        val fragmentAdapter =
            MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)

        // --------------- TAB



        // --------------- Spinner

        val spinner = findViewById<Spinner>(R.id.spinnerActivities)
        var list_of_items = arrayOf("Marketing", "Administration", "Comptabilité")



        if (spinner != null) {
            val adapter = ArrayAdapter(this, R.layout.spinner_color, list_of_items)


            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

//                    activitiesSelect.text = list_of_items[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {



                }



            }



        }


        // --------------- Spinner




    }



}
