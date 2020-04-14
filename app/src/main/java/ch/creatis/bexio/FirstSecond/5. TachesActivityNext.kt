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


        // --------------------------------------------------- Intent

        // Chaque DAO a une méthode qui lui permet de retrouver selon  l'id fournit, à quelle objet il appartient
        var project_id = intent.getIntExtra("project_id",0)
        var subject = intent.getStringExtra("subject")
        var user_id = intent.getIntExtra("user_id",0)
        var finish_date = intent.getStringExtra("finish_date")
        var contact_id = intent.getIntExtra("contact_id",0)
        // Reprend directement sa valeur
        var todo_status_id = intent.getIntExtra("todo_status_id",0)



        // --------------------------------------------------- Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val projetsDAO = database.projetDAO
        val usersDAO = database.userDAO
        val contactsDAO = database.contactDAO



        // --------------------------------------------------- Les variables Projet

        // Projet ID
        var projetId = ""
        if (project_id == 0){ projetId = "" } else{
            projetId = projetsDAO.getItemsByIdbexio(project_id).name.toString()
        }

        // Users
        var userId = usersDAO.getItemsByIdbexio(user_id)

        // Contacts
        var contactId = contactsDAO.getItemsByIdbexio(contact_id)



        // --------------------------------------------------- Les labels
        projectName.text = projetId
        tacheName.text = subject
//        interlocuteurName.text = userId.firstname + " " + userId.lastname
        dureeName.text = finish_date
//        contactName.text = contactId.name_un + " " + contactId.name_deux



        if(todo_status_id == 1){
            statutName.text = "En suspens"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        } else if (todo_status_id == 5){
            statutName.text = "Terminé"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_termine)
        }



        // --------------------------------------------------- Les boutons

        // Saisir un temps
        saisirTempsTache.setOnClickListener {
            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)
        }



        // Tâche effectuée
        tacheEffectue.setOnClickListener {



        }



    }



}