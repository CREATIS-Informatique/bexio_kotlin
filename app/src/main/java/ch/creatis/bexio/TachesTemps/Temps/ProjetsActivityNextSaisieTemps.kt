package ch.creatis.bexio.TachesTemps.Temps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.room.Room
import ch.creatis.bexio.TachesTemps.Temps.Pager.MyPagerAdapter
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.Activite
import ch.creatis.bexio.Room.AppDatabase
import kotlinx.android.synthetic.main.activity_projets_next_saisie_temps.*

class ProjetsActivityNextSaisieTemps : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next_saisie_temps)



        // ---------------------------------------------------- TAB

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)



        // --------------------------------------------- Spinner

        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val activiteDAO = database.activiteDAO
        var activiteList = activiteDAO.getItems() as ArrayList<Activite>
        var activiteListFiltered = mutableListOf<String>()
        for (activite in activiteList){ activiteListFiltered.add(activite.name!!) }



        val spinner = findViewById<Spinner>(R.id.spinnerActivities)



        if (spinner != null) {
            

            val adapter = ArrayAdapter(this, R.layout.spinner_color, activiteListFiltered)


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
