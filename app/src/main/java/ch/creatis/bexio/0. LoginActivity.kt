package ch.creatis.bexio



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.room.Room
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Contact
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject



class LoginActivity : AppCompatActivity() {

    

    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false

    var contactListDatabase = mutableListOf<Contact>()



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")
        if (refreshToken == ""){ webViewIsVisible() } else {getAccessTokenAllTime()}



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    fun webViewIsVisible(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()



        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {



            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url!!.contains("code")) {



                    val chars = url
                    val codeun = chars!!.dropLast(18)



                    // -------------------------------------------------------------------------------------

                        editor.putString("CODETOKEN", codeun.drop(16))
                        editor.commit()
                        getAccessTokenFirstTime()

                    // -------------------------------------------------------------------------------------



                }
            }
        }



        webView.loadUrl("https://office.bexio.com/oauth/authorize?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&state=creatis2019&scope=contact_show project_show task_show monitoring_edit")



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    fun getAccessTokenFirstTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var codeToken = sharedPreferences.getString("CODETOKEN", "")



        val queue = Volley.newRequestQueue(this)

        val url = "https://office.bexio.com/oauth/access_token?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&client_secret=nRA79oKLwIIRh7NOH5TNavWabEE=&$codeToken"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->

            var responseJsonObj = JSONObject(response)


            // -------------------------------------------------------------------------------------

            webView.visibility = View.INVISIBLE
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("ORG", responseJsonObj.getString("org"))
            editor.commit()
            makeAllDataRequest()

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }



    fun getAccessTokenAllTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")



        val queue = Volley.newRequestQueue(this)

        val url = "https://office.bexio.com/oauth/refresh_token?client_id=4707003559.apps.bexio.com&client_secret=nRA79oKLwIIRh7NOH5TNavWabEE=&refresh_token=$refreshToken"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->



            // -------------------------------------------------------------------------------------

            var responseJsonObj = JSONObject(response)
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("ORG", responseJsonObj.getString("org"))
            editor.commit()



            makeAllDataRequest()

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    fun makeAllDataRequest(){



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val org = sharedPreferences.getString("ORG", "")
        val url = "https://office.bexio.com/api2.php/$org/contact"
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonArrayRequest(Method.GET, url, JSONArray(), Response.Listener<JSONArray> { response ->

                for (i in 0 until response.length()) {
                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name= response.getJSONObject(i)["name_1"].toString()
                    val address= response.getJSONObject(i)["address"].toString()
                    val postcode= response.getJSONObject(i)["postcode"].toString()
                    val city= response.getJSONObject(i)["city"].toString()
                    val mail= response.getJSONObject(i)["mail"].toString()
                    val phone_fixed= response.getJSONObject(i)["phone_fixed"].toString()
                    val contact = Contact(null, idBexio,name,address,postcode,city,mail,phone_fixed)
                    contactListDatabase.add(contact)
                }


            numberOfRequestsToMake--
            if (numberOfRequestsToMake == 0) { requestEndInternet() }



            }, Response.ErrorListener {


            numberOfRequestsToMake--
            hasRequestFailed = true
            if (numberOfRequestsToMake == 0) { requestEndInternet() }


        })

        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $accessToken"
                return headers
            }
        }



        queue.add(stringRequest)
        numberOfRequestsToMake++



    }



    fun requestEndInternet() {

        if (hasRequestFailed) {
            hasRequestFailed = false


        } else {

            updateDatabase()

        }



    }



    fun updateDatabase(){



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()



        val contactDAO = database.contactDAO
        contactDAO.delete()
        for (contact in contactListDatabase){ contactDAO.insert(contact)}



        val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentAct)



    }



}