package ch.creatis.bexio.TachesTemps.Temps.FirstSecond

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.creatis.bexio.R
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.z_fragment_activity_projets_next_saisie_temps_second.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ActivityProjetsNextSaisieTempsSecond : Fragment() {



    var startDuree = ""
    var startDate = ""
    var endDuree = ""
    var endDate = ""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.z_fragment_activity_projets_next_saisie_temps_second, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        // -----------------------------------------------------------------------------------------

        val calendrierDuree = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendrierDuree.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDuree.set(Calendar.MINUTE, minute)
            var dureeInfoDebut = SimpleDateFormat("HH:mm:ss").format(calendrierDuree.time)
            startDuree = dureeInfoDebut



            // version suisse
            jourDebutHeureRappel.text = SimpleDateFormat("HH:mm").format(calendrierDuree.time)

        }



        jourDebutTimeButton.setOnClickListener { TimePickerDialog(context, timeSetListener, calendrierDuree.get(Calendar.HOUR_OF_DAY), calendrierDuree.get(Calendar.MINUTE), true).show()}







        // -----------------------------------------------------------------------------------------

        var calendrierDateDebut = Calendar.getInstance()
        val dateSetListenerDebut = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendrierDateDebut.set(Calendar.YEAR, year)
            calendrierDateDebut.set(Calendar.MONTH, monthOfYear)
            calendrierDateDebut.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            startDate = sdf.format(calendrierDateDebut.time)



            // Version suisse
            val myFormatTwo = "dd.MM.yyyy"
            val sdfTwo = SimpleDateFormat(myFormatTwo, Locale.US)
            jourDebutDateRappel.text = sdfTwo.format(calendrierDateDebut.time)

        }



        jourDebutCalendarButton.setOnClickListener { DatePickerDialog(context!!, dateSetListenerDebut, calendrierDateDebut.get(Calendar.YEAR), calendrierDateDebut.get(Calendar.MONTH), calendrierDateDebut.get(Calendar.DAY_OF_MONTH)).show() }







        // -----------------------------------------------------------------------------------------

        val calendrierDureeFin = Calendar.getInstance()
        val timeSetListenerFin = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendrierDureeFin.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDureeFin.set(Calendar.MINUTE, minute)
            var dureeInfoFin = SimpleDateFormat("HH:mm:ss").format(calendrierDureeFin.time)
            endDuree = dureeInfoFin



            // version suisse
            jourFinHeureRappel.text = SimpleDateFormat("HH:mm").format(calendrierDuree.time)
        }



        jourFinTimeButton.setOnClickListener { TimePickerDialog(context, timeSetListenerFin, calendrierDureeFin.get(Calendar.HOUR_OF_DAY), calendrierDureeFin.get(Calendar.MINUTE), true).show() }







        // -----------------------------------------------------------------------------------------


        var calendrierDateFin = Calendar.getInstance()
        val dateSetListenerFin = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendrierDateFin.set(Calendar.YEAR, year)
            calendrierDateFin.set(Calendar.MONTH, monthOfYear)
            calendrierDateFin.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            endDate = sdf.format(calendrierDateFin.time)



            // Version suisse
            val myFormatTwo = "dd.MM.yyyy"
            val sdfTwo = SimpleDateFormat(myFormatTwo, Locale.US)
            jourFinDateRappel.text = sdfTwo.format(calendrierDateFin.time)


        }


        jourFinCalendarButton.setOnClickListener { DatePickerDialog(context!!, dateSetListenerFin, calendrierDateFin.get(Calendar.YEAR), calendrierDateFin.get(Calendar.MONTH), calendrierDateFin.get(Calendar.DAY_OF_MONTH)).show() }





        // -----------------------------------------------------------------------------------------

        envoyerPlusieurJourButton.setOnClickListener {



            val builder = AlertDialog.Builder(context)
            builder.setTitle("Voulez-vous envoyer votre timbrage ?")
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

        var startFinal = startDate + startDuree
        var endfinal = endDate + endDuree



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = context!!.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
        val companyUserIdDecode = sharedPreferences.getString("companyUserIdDecode", "")!!.toInt()

        // -----------------------------------------------------------------------------------------



        var url = "https://api.bexio.com/2.0/timesheet"



        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        val jsonObjectTracking = JSONObject()

        try
        {
            jsonObjectTracking.put("type", "range")
            jsonObjectTracking.put("start", startFinal)
            jsonObjectTracking.put("end", endfinal)

        }
        catch (e:Exception) {
        }



        try
        {
            jsonObject.put("user_id", companyUserIdDecode)
            jsonObject.put("client_service_id", 2)
            jsonObject.put("allowable_bill", false)
            jsonObject.put("tracking", jsonObjectTracking)
            jsonArray.put(jsonObject)
        }
        catch (e:Exception) {
        }



        val stringRequest = object: JsonObjectRequest(
            Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {



                override fun onResponse(response: JSONObject?) {



                    // Message Finale
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Envoi réussi !")
                    builder.setMessage("Vos timbrages ont été actualisés")
                    builder.setPositiveButton("Ok"){ _, _ ->
                        // Lance l'activité
                        getActivity()!!.onBackPressed()

                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()



                } },



            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {



                    // Message Finale
                    val builder = AlertDialog.Builder(context)
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



        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)



    }



}
