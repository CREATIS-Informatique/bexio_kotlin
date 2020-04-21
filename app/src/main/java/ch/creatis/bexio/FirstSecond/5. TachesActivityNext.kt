package ch.creatis.bexio.FirstSecond



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import ch.creatis.bexio.TachesTemps.Temps.First.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_taches_next.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class TachesActivityNext : AppCompatActivity() {



    // Pour la requête Tache effectuee
    var idBexio: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taches_next)



        // --------------------------------------------------- Intent

        // Chaque DAO a une méthode qui lui permet de retrouver selon  l'id fournit, à quelle objet il appartient
        idBexio = intent.getIntExtra("idBexio",0)
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



        // Ne passe pas par une donnée de Room
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
        if(todo_status_id == 5){
            tacheEffectue.text = "TÂCHE EN SUSPSENS"
            tacheEffectue.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        } else if(todo_status_id == 1){
            tacheEffectue.text = "TÂCHE TERMINE"
            tacheEffectue.setBackgroundResource(R.drawable.taches_activity_items_status_termine)
        }



        tacheEffectue.setOnClickListener {

            TacheEffectue()

        }



    }



    // --------------------------------------------------- Les Fonctions de la classe

    fun TacheEffectue(){

        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        var url = "https://api.bexio.com/2.0/task/$idBexio"



        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        try
        {
            jsonObject.put("todo_status_id", 5)
            jsonArray.put(jsonObject)
        }
        catch (e:Exception) {
        }



        val stringRequest = object: JsonObjectRequest(
            Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {

                override fun onResponse(response: JSONObject?) {



                } },



            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Log.e("Error.Response", error.toString())

                }
            })



        {
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json")
                headers.put("Content-Type", "application/json")
                headers.put("Authorization", "Bearer $accessToken")
                return headers
            }
        }



        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }



}