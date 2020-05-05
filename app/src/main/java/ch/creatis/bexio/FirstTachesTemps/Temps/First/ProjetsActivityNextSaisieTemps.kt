package ch.creatis.bexio.FirstTachesTemps.Temps.First

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.room.Room
import ch.creatis.bexio.FirstTachesTemps.Temps.FirstSecond.Pager.MyPagerAdapter
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.Activite
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Contact
import kotlinx.android.synthetic.main.z_activity_projets_next_saisie_temps.*

class ProjetsActivityNextSaisieTemps : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.z_activity_projets_next_saisie_temps)



        // La variable qui est utilisée pour connaitre l'ID du project lors d'une saise de temps
        // Si il ne provient pas d'un intent, l'utilisateur est donc considéré comme provenant directement de temps
        val bundle=intent.extras
        if(bundle!=null)
        {
            IDBEXIOPROJECTCOMPANION = bundle.getString("project_id_from_project_activity").toString()
        }



        // Prénom et Nom
        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var givenName = sharedPreferences.getString("given_name", "")
        var familyName = sharedPreferences.getString("family_name", "")
        println("Here Here Here Here Here Here Here Here Here Here Here Here HereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHereHere ")
        println(givenName)
        println(familyName)



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
            val adapter = ArrayAdapter(this, R.layout.y_spinner_color, activiteListFiltered)


            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    // Permet de trouver l'ID de l'activité grâce à son nom
                    var You = activiteDAO.getItemsIDByName(activiteListFiltered[position])
                    IDBEXIOACTIVITATECOMPANION = You.idBexio!!.toInt()


                }

                override fun onNothingSelected(parent: AdapterView<*>) {



                }



            }



        }


        // --------------- Spinner




    }



    companion object {

        // La variable qui est utilisée pour connaitre l'ID du project lors d'une saise de temps
        var IDBEXIOPROJECTCOMPANION = "null"
        var IDBEXIOACTIVITATECOMPANION = 0

    }



}
