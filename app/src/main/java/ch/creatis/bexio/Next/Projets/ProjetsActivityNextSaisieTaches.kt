package ch.creatis.bexio.Next.Projets

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import ch.creatis.bexio.R
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_projets_next.*
import kotlinx.android.synthetic.main.activity_projets_next_saisie_taches.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ProjetsActivityNextSaisieTaches : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next_saisie_taches)


        var date = ""
        var temps = ""


        // ------------------------------- Calendar -------------------------------

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            date = sdf.format(cal.time)
            dateLimite.text = date


        }


        calendarButton.setOnClickListener {

            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }



        // ------------------------------- Time -------------------------------


        val cal2 = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal2.set(Calendar.HOUR_OF_DAY, hour)
            cal2.set(Calendar.MINUTE, minute)

            temps = SimpleDateFormat("HH:mm").format(cal2.time)
            dateLimite.text = date + " - " + temps



        }


            timeButton.setOnClickListener {

            TimePickerDialog(this, timeSetListener, cal2.get(Calendar.HOUR_OF_DAY), cal2.get(
                Calendar.MINUTE), true).show()


        }



        // ------------------------------- Time -------------------------------

        envoyerButton.setOnClickListener {

            println(objetInput.text)
            println(remarquesInput.text)



            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            var url = "https://api.bexio.com/2.0/task"



            val jsonArray = JSONArray()
            val jsonObject = JSONObject()
            try
            {
                jsonObject.put("user_id", 1)
                jsonObject.put("subject", "TESTESTESTEST")
                jsonArray.put(jsonObject)
            }
            catch (e:Exception) {
            }



            val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject, object: Response.Listener<JSONObject> {

                    override fun onResponse(response: JSONObject?) {



                    } },



                object: Response.ErrorListener {
                    override fun onErrorResponse(error:VolleyError) {
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



}