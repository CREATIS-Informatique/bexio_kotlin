package ch.creatis.bexio.FirstSecond.Temps.FirstSecond



import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_temps_second_items_next_jour_description.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class TempsActivityItemsNextJourDescription : AppCompatActivity() {



    var idBexio = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps_second_items_next_jour_description)



        idBexio = intent.getIntExtra("idBexio",0)
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
        interlocuteurTacheLabel.text = userDAOFinal.firstname + " " + userDAOFinal.lastname
        descriptionLabel.text = text



        deleteTimeButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Voulez-vous supprimer ce temps ?")
            builder.setMessage("L'application mettra à jour cette donnée")
            builder.setPositiveButton("Oui"){ _, _ ->
                // Envoi les datas
                deleteTimeSheet()
            }
            builder.setNeutralButton("Annuler"){_,_ ->
                // Ne fait rien
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()


        }



    }



    fun deleteTimeSheet(){

        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        var url = "https://api.bexio.com/2.0/timesheet/$idBexio"



        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        try
        {
            jsonObject.put("timesheet_id", idBexio)
            jsonArray.put(jsonObject)
        }
        catch (e:Exception) {
        }



        val stringRequest = object: JsonObjectRequest(Method.DELETE, url, jsonObject, object: Response.Listener<JSONObject> {

                override fun onResponse(response: JSONObject?) {



                    // Message Finale
                    val builder = AlertDialog.Builder(this@TempsActivityItemsNextJourDescription)
                    builder.setTitle("Supression réussie !")
                    builder.setMessage("Vos temps ont été actualisés")
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
                    val builder = AlertDialog.Builder(this@TempsActivityItemsNextJourDescription)
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
