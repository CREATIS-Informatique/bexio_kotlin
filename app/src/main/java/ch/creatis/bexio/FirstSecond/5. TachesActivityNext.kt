package ch.creatis.bexio.FirstSecond



import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
        if (project_id == 0){ projetId = "" } else{ projetId = projetsDAO.getItemsByIdbexio(project_id).name.toString() }
        // Users
        var userId = usersDAO.getItemsByIdbexio(user_id)
        // Contacts
        var contactId = contactsDAO.getItemsByIdbexio(contact_id)



        // --------------------------------------------------- Les labels

        tacheName.text = subject
        projectName.text = "Aucune indication"
        interlocuteurName.text = "Aucune indication"



        var inputText = finish_date
        var inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var dateInput = inputFormat.parse(inputText)
        var outputFormat: DateFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.US)
        var dateOutput = outputFormat.format(dateInput)
        dureeName.text = dateOutput



        priorityName.text = "Aucune indication"
        contactName.text = "Aucune indication"



        // Statut - 2020-04-29 13:15:00

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val currentDate = sdf.format(Date())

//        int compare = date1.compareTo(date2);
//
//        compare > 0, if date2 is greater than date1
//
//        compare < 0, if date2 is smaller than date1
//
//        compare = 0, if date1 is equal to date2

        if(todo_status_id == 1 && finish_date.isNullOrEmpty()){
            statutName.text = "En suspens"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        }

        else if (todo_status_id == 1 && finish_date.compareTo(currentDate) < 0){
            statutName.text = "En retard"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_en_retard)
        }

        else if (todo_status_id == 1 && finish_date.compareTo(currentDate) > 0){
            statutName.text = "En suspens"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        }

        else if (todo_status_id == 5){
            statutName.text = "Terminé"
            statutName.setBackgroundResource(R.drawable.taches_activity_items_status_termine)
        }



        // ---------------------------------------------------------- Les boutons ----------------------------------------------------------

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



            // La tâche sera marquée comme en suspens
            if(todo_status_id == 5){



                val builder = AlertDialog.Builder(this)
                builder.setTitle("Voulez-vous marquer votre tâche en suspens ?")
                builder.setMessage("L'application mettra à jour cette donnée")
                builder.setPositiveButton("Oui"){ _, _ ->
                    // Envoi les datas
                    TacheEnSuspens()
                }
                builder.setNeutralButton("Annuler"){_,_ ->
                    // Ne fait rien
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()



            }



            // La tâche sera marquée comme terminée
            else if(todo_status_id == 1){



                val builder = AlertDialog.Builder(this)
                builder.setTitle("Voulez-vous marquer votre tâche comme terminée ?")
                builder.setMessage("L'application mettra à jour cette donnée")
                builder.setPositiveButton("Oui"){ _, _ ->
                    // Envoi les datas
                    TacheEffectue()
                }
                builder.setNeutralButton("Annuler"){_,_ ->
                    // Ne fait rien
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()



            }



        }



    }



    // ------------------------------------------------------------------------------------------------------------------------------------------------------



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



        val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {

                override fun onResponse(response: JSONObject?) {



                    // Message Finale
                    val builder = AlertDialog.Builder(this@TachesActivityNext)
                    builder.setTitle("Envoi réussi !")
                    builder.setMessage("Vos tâches ont été actualisés")
                    builder.setPositiveButton("Ok"){ _, _ ->
                        // Lance l'activité
                        finish()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()



                } },



            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {



                    // Message Finale
                    val builder = AlertDialog.Builder(this@TachesActivityNext)
                    builder.setTitle("L'envoi a échoué !")
                    builder.setMessage("Essayez à nouveau")
                    builder.setPositiveButton("Ok"){ _, _ ->
                        // Ne fait rien !
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()



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



    fun TacheEnSuspens(){

        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        var url = "https://api.bexio.com/2.0/task/$idBexio"



        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        try
        {
            jsonObject.put("todo_status_id", 1)
            jsonArray.put(jsonObject)
        }
        catch (e:Exception) {
        }



        val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {

            override fun onResponse(response: JSONObject?) {



                // Message Finale
                val builder = AlertDialog.Builder(this@TachesActivityNext)
                builder.setTitle("Envoi réussi !")
                builder.setMessage("Vos tâches ont été actualisés")
                builder.setPositiveButton("Ok"){ _, _ ->
                    // Lance l'activité
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()



            } },



            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {



                    // Message Finale
                    val builder = AlertDialog.Builder(this@TachesActivityNext)
                    builder.setTitle("L'envoi a échoué !")
                    builder.setMessage("Essayez à nouveau")
                    builder.setPositiveButton("Ok"){ _, _ ->
                        // Ne fait rien !
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()



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