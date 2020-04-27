package ch.creatis.bexio.FirstSecond.Temps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import kotlinx.android.synthetic.main.activity_temps_items_next_jour_description.*

class TempsActivityItemsNextJourDescription : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps_items_next_jour_description)



        var idBexio = intent.getStringExtra("idBexio").toInt()
        var date = intent.getStringExtra("date")
        var duration = intent.getStringExtra("duration")
        var semaine = intent.getStringExtra("semaine")
        var annee = intent.getStringExtra("annee")
        var text = intent.getStringExtra("text")



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val contactDAO = database.contactDAO
        val contactDAOFinal = contactDAO.getItemsByIdbexio(idBexio)



        // -----------------------------------------------------------------------------------------



        dateLabel.text = date
        projetLabel.text = "Test"
        dureeLabel.text = duration
        interlocuteurLabel.text = contactDAOFinal.name_un + contactDAOFinal.name_deux



    }



}
