package ch.creatis.bexio.FirstSecond



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ch.creatis.bexio.TachesTemps.Temps.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import kotlinx.android.synthetic.main.activity_taches_next.*



class TachesActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taches_next)


        var project_id = intent.getIntExtra("project_id",0)
        var subject = intent.getStringExtra("subject")
        var user_id = intent.getIntExtra("user_id",0)
        var finish_date = intent.getStringExtra("finish_date")
        var contact_id = intent.getIntExtra("contact_id",0)



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val projetsDAO = database.projetDAO
        var projetId = ""
        if (project_id == 0){ projetId = "" } else{ projetId = projetsDAO.getItemsByIdbexio(project_id).name!! }
        val usersDAO = database.userDAO
        var userId = usersDAO.getItemsByIdbexio(user_id)
        val contactsDAO = database.contactDAO
        var contactId = contactsDAO.getItemsByIdbexio(contact_id)



        projectName.text = projetId
        tacheName.text = subject
        interlocuteurName.text = userId.firstname + " " + userId.lastname
        dureeName.text = finish_date
        contactName.text = contactId.name_un + " " + contactId.name_deux



        saisirTempsTache.setOnClickListener {

            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)

        }



    }



}