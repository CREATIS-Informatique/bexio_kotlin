package ch.creatis.bexio.TachesTemps.Temps.Pager



import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.creatis.bexio.R
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_activity_projets_next_saisie_temps_first.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*



class ActivityProjetsNextSaisieTempsFirst : Fragment() {



    var dureeInfo = ""
    var dateInfo = ""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_activity_projets_next_saisie_temps_first, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Durée
        val calendrierDuree = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendrierDuree.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDuree.set(Calendar.MINUTE, minute)
            dureeInfo = SimpleDateFormat("HH:mm").format(calendrierDuree.time)
            dureeRappel.text = dureeInfo

        }

        // Button Durée
        timePicker.setOnClickListener {
            TimePickerDialog(context, timeSetListener, calendrierDuree.get(Calendar.HOUR_OF_DAY), calendrierDuree.get(Calendar.MINUTE), true).show()
        }



        // -------------------------------------------------------------------------------------------------------------------------------



        // Date
        var calendrierDate = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendrierDate.set(Calendar.YEAR, year)
            calendrierDate.set(Calendar.MONTH, monthOfYear)
            calendrierDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateInfo = sdf.format(calendrierDate.time)



            // Version suisse
            val myFormatTwo = "dd.MM.yyyy"
            val sdfTwo = SimpleDateFormat(myFormatTwo, Locale.US)
            dateRappel.text = sdfTwo.format(calendrierDate.time)



        }

        // Button Date
        datePicker.setOnClickListener {
            DatePickerDialog(context!!, dateSetListener,
                calendrierDate.get(Calendar.YEAR),
                calendrierDate.get(Calendar.MONTH),
                calendrierDate.get(Calendar.DAY_OF_MONTH)).show()
        }



        // -------------------------------------------------------------------------------------------------------------------------------



        envoyerUnJourButton.setOnClickListener {

            // -----------------------------------------------------------------------------------------

            val sharedPreferences = context!!.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            var url = "https://api.bexio.com/2.0/timesheet"



            val jsonArray = JSONArray()
            val jsonObject = JSONObject()
            val jsonObjectTracking = JSONObject()

            try
            {
                jsonObjectTracking.put("type", "duration")
                jsonObjectTracking.put("date", dateInfo)
                jsonObjectTracking.put("duration", dureeInfo)

            }
            catch (e:Exception) {
            }



            try
            {
                jsonObject.put("user_id", 2)
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
                            println(response)


                    } },



                object: Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        println(error)

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



}