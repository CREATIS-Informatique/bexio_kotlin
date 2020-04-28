package ch.creatis.bexio.TachesTemps.Taches

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.R
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.z_activity_projets_next_saisie_taches.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ProjetsActivityNextSaisieTaches : AppCompatActivity() {



    var dateFinal = ""
    var heureFinal = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.z_activity_projets_next_saisie_taches)





        // ------------------------------- Date -------------------------------

        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateFinal = sdf.format(cal.time)



            dateLimite.text = dateFinal



        }


        calendarButton.setOnClickListener {

            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

        }



        // ------------------------------- Time -------------------------------


        val cal2 = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal2.set(Calendar.HOUR_OF_DAY, hour)
            cal2.set(Calendar.MINUTE, minute)
            heureFinal = SimpleDateFormat("HH:mm").format(cal2.time)



            heureLimite.text = heureFinal



        }



            timeButton.setOnClickListener {

            TimePickerDialog(this, timeSetListener, cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), true).show()

        }



        // --------------------------------------------------------------

        envoyerButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Voulez-vous envoyer votre tâche ?")
            builder.setMessage("L'application mettra à jour vos données")
            builder.setPositiveButton("Oui"){ _, _ ->

                // Envoi les datas
                sendData()

            }

            builder.setNeutralButton("Annuler"){_,_ ->
                // Ne fait rien
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }



    }



    // --------------------------------------------------------------- onCreate -------------------------------------------------------------



    fun sendData(){



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
        val companyUserIdDecode = sharedPreferences.getString("companyUserIdDecode", "")!!.toInt()

        // -----------------------------------------------------------------------------------------



        var url = "https://api.bexio.com/2.0/task"



        var inputText = dateFinal
        var inputFormat:DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        var dateInput = inputFormat.parse(inputText)
        var outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var dateOutput = outputFormat.format(dateInput)



        var dateFinalFinal = dateOutput
        var heureFinalFinal = heureFinal + ":00"
        var timeStampFinalFinal = dateFinalFinal + heureFinalFinal



        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        try
        {
            jsonObject.put("user_id", companyUserIdDecode)
            jsonObject.put("finish_date", timeStampFinalFinal)
            jsonObject.put("subject", objetInputText.text)
            jsonObject.put("info", remarquesInputText.text)
            jsonArray.put(jsonObject)
        }
        catch (e:Exception) {
        }



        val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {

            override fun onResponse(response: JSONObject?) {



                // Message Finale
                val builder = AlertDialog.Builder(this@ProjetsActivityNextSaisieTaches)
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
                override fun onErrorResponse(error:VolleyError) {



                    // Message Finale
                    val builder = AlertDialog.Builder(this@ProjetsActivityNextSaisieTaches)
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