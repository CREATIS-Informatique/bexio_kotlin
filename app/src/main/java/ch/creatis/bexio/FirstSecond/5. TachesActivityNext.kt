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



        // Chaque DAO a une méthode qui lui permet de retrouver selon  l'id fournit, à quelle objet il appartient



        var project_id = intent.getIntExtra("project_id",0)
        var subject = intent.getStringExtra("subject")
        var user_id = intent.getIntExtra("user_id",0)
        var finish_date = intent.getStringExtra("finish_date")
        var contact_id = intent.getIntExtra("contact_id",0)



        // Init database globale
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()



        // Projets
        val projetsDAO = database.projetDAO
        var projetId = ""
        if (project_id == 0){ projetId = "" } else{
            projetId = projetsDAO.getItemsByIdbexio(project_id).name.toString()
        }

        // Users
        val usersDAO = database.userDAO
        var userId = usersDAO.getItemsByIdbexio(user_id)

        // Contacts
        val contactsDAO = database.contactDAO
        var contactId = contactsDAO.getItemsByIdbexio(contact_id)



        projectName.text = projetId
        tacheName.text = subject
//        interlocuteurName.text = userId.firstname + " " + userId.lastname
        dureeName.text = finish_date
//        contactName.text = contactId.name_un + " " + contactId.name_deux



        saisirTempsTache.setOnClickListener {
            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)
        }



    }



}