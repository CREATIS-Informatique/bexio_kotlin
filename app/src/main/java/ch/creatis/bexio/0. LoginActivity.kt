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
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class LoginActivity : AppCompatActivity() {



    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false
    var contactListDatabase = mutableListOf<Contact>()



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var clientidsettings = sharedPreferences.getString("CLIENTID", "")
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")



        // Première installation de l'application si le Refresh Token n'est pas vide alors il requête avec le Refresh Token
        if(refreshToken == ""){

            webViewIsVisible() }

        else {

            getAccessTokenAllTime()

        }



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    // 1
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

                        editor.putString("CODETOKEN", codeun.drop(15))
                        editor.commit()
                        getAccessTokenFirstTime()

                    // -------------------------------------------------------------------------------------



                }
            }
        }



        webView.loadUrl("https://idp.bexio.com/authorize?response_type=code&client_id=7baa8853-0f2d-48f5-aa7e-1baf94879d53&redirect_uri=my://demo&state=creatis2019&scope=openid profile offline_access contact_show project_show task_edit task_show monitoring_edit")



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    // 2
    fun getAccessTokenFirstTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var codeToken = sharedPreferences.getString("CODETOKEN", "")
        val url = "https://idp.bexio.com/token?grant_type=authorization_code&code=$codeToken&client_id=7baa8853-0f2d-48f5-aa7e-1baf94879d53&redirect_uri=my://demo&client_secret=H_qo-zE3jEsxGGhkwu9Cfv4UnUtXPXadKGqvQ47d-i94nojffdpVcQKqOErTVZc4Zbuyw_lVWjKAZpj6uor-7w"



        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->

            var responseJsonObj = JSONObject(response)
            println(response)

            // -------------------------------------------------------------------------------------

            webView.visibility = View.INVISIBLE
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("IDTOKEN", responseJsonObj.getString("id_token"))
            editor.commit()
            makeAllDataRequest()

            // -------------------------------------------------------------------------------------

        }, Response.ErrorListener {})



        queue.add(stringRequest)



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    // Last
    fun getAccessTokenAllTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")



        val queue = Volley.newRequestQueue(this)

        val url = "https://idp.bexio.com/token?grant_type=refresh_token&refresh_token=$refreshToken&client_id=7baa8853-0f2d-48f5-aa7e-1baf94879d53&client_secret=H_qo-zE3jEsxGGhkwu9Cfv4UnUtXPXadKGqvQ47d-i94nojffdpVcQKqOErTVZc4Zbuyw_lVWjKAZpj6uor-7w"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->



            // -------------------------------------------------------------------------------------

            var responseJsonObj = JSONObject(response)
            println(response)
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("IDTOKEN", responseJsonObj.getString("id_token"))
            editor.commit()



            makeAllDataRequest()

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }































































































    // 3
    fun makeAllDataRequest(){



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val url = "https://api.bexio.com/2.0/contact"
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonArrayRequest(Method.GET, url, JSONArray(), Response.Listener<JSONArray> { response ->

                for (i in 0 until response.length()) {
                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name_un= response.getJSONObject(i)["name_1"].toString()
                    val name_deux= response.getJSONObject(i)["name_2"].toString()
                    val address= response.getJSONObject(i)["address"].toString()
                    val postcode= response.getJSONObject(i)["postcode"].toString()
                    val city= response.getJSONObject(i)["city"].toString()
                    val country_id = response.getJSONObject(i)["country_id"].toString()
                    val mail= response.getJSONObject(i)["mail"].toString()
                    val mail_second= response.getJSONObject(i)["mail_second"].toString()
                    val phone_fixed= response.getJSONObject(i)["phone_fixed"].toString()
                    val phone_fixed_second= response.getJSONObject(i)["phone_fixed_second"].toString()
                    val phone_mobile= response.getJSONObject(i)["phone_mobile"].toString()
                    val fax= response.getJSONObject(i)["fax"].toString()
                    val url= response.getJSONObject(i)["url"].toString()
                    val skype_name= response.getJSONObject(i)["skype_name"].toString()
                    val contact = Contact(null, idBexio,name_un, name_deux,address,postcode,city,country_id,mail,mail_second,phone_fixed,phone_fixed_second,phone_mobile,fax,url,skype_name)
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



    // 4
    fun requestEndInternet() {

        if (hasRequestFailed) {
            hasRequestFailed = false


        } else {

            updateDatabase()

        }



    }



    // 5
    fun updateDatabase(){



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").fallbackToDestructiveMigration().allowMainThreadQueries().build()



        val contactDAO = database.contactDAO
        contactDAO.delete()
        for (contact in contactListDatabase){ contactDAO.insert(contact)}



        val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentAct)



    }



}