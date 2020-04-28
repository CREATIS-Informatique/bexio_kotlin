package ch.creatis.bexio.FirstSecond.Temps.FirstSecond



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import kotlinx.android.synthetic.main.activity_temps_second_items_next_jour_description.*



class TempsActivityItemsNextJourDescription : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps_second_items_next_jour_description)



        var idBexio = intent.getIntExtra("idBexio",0)
        var userID = intent.getIntExtra("userId",0)
        var client_service_id = intent.getIntExtra("client_service_id",0)
        var text = intent.getStringExtra("text")
        var pr_project_id = intent.getIntExtra("pr_project_id",0)
        var date = intent.getStringExtra("date")
        var duration = intent.getStringExtra("duration")
        var semaine = intent.getStringExtra("semaine")
        var annee = intent.getStringExtra("annee")



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()

        // Projet
        var projetDAOFinal = "Aucun projet associé"
        val projetDAO = database.projetDAO
        if (pr_project_id != 0){ projetDAOFinal = projetDAO.getItemsByIdbexio(pr_project_id).name.toString() }

        // Activité
        val activiteDAO = database.activiteDAO
        val activiteDAOFinal = activiteDAO.getItemsByIdbexio(client_service_id)

        // Interlocuteur
        val userDAO = database.userDAO
        val userDAOFinal = userDAO.getItemsByIdbexio(userID)



        // -----------------------------------------------------------------------------------------



        dateLabel.text = date
        projetLabel.text = projetDAOFinal
        activiteLabel.text = activiteDAOFinal.name
        dureeLabel.text = duration
        interlocuteurLabel.text = userDAOFinal.firstname + " " + userDAOFinal.lastname
        descriptionLabel.text = text






    }



}
