package ch.creatis.bexio.Login



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.room.Room
import ch.creatis.bexio.MainActivity.MainActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap



class LoginActivity : AppCompatActivity() {



    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false



    var activiteListDatabase = mutableListOf<Activite>()
    var contactListDatabase = mutableListOf<Contact>()
    var projetListDatabase = mutableListOf<Projet>()
    var tacheListDatabase= mutableListOf<Tache>()
    var tempsListDatabase = mutableListOf<Temps>()
    var userListDatabase = mutableListOf<User>()















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



        webView.loadUrl("https://idp.bexio.com/authorize?response_type=code&client_id=7baa8853-0f2d-48f5-aa7e-1baf94879d53&redirect_uri=my://demo&state=creatis2019&scope= openid profile offline_access contact_show project_show task_edit task_show monitoring_edit")



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



            // -------------------------------------------------------------------------------------

            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("IDTOKEN", responseJsonObj.getString("id_token"))
            editor.commit()
            makeAllDataRequest()



            // Cache la WebView
            webView.visibility = View.INVISIBLE

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
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("IDTOKEN", responseJsonObj.getString("id_token"))
            editor.commit()



            userInfos()



            makeAllDataRequest()

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }



    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    // String Request
    // Header

    fun userInfos(){



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
        println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
        println(accessToken)

        val idtoken = sharedPreferences.getString("IDTOKEN", "")
        println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
        println(idtoken)

        // -----------------------------------------------------------------------------------------



        val url = "https://idp.bexio.com/userinfo"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object: StringRequest(Method.GET, url, Response.Listener<String> { response ->


            var responseJsonObj = JSONObject(response)
            editor.putString("sub", responseJsonObj.getString("sub"))
            editor.putString("given_name", responseJsonObj.getString("given_name"))
            editor.putString("family_name", responseJsonObj.getString("family_name"))
            editor.commit()

            numberOfRequestsToMake--
            if (numberOfRequestsToMake == 0) { requestEndInternet() }
        },
            Response.ErrorListener {
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




















































































    // 3
    fun makeAllDataRequest(){



        // -----------------------------------------------------------------------------------------



        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlActivite = "https://api.bexio.com/2.0/client_service"
        val queueActivite = Volley.newRequestQueue(this)
        val stringRequestActivite = object : JsonArrayRequest(Method.GET, urlActivite, JSONArray(), Response.Listener<JSONArray> { response ->

            for (i in 0 until response.length()) {
                val idBexio= response.getJSONObject(i)["id"].toString()
                val name= response.getJSONObject(i)["name"].toString()
                val default_is_billable= response.getJSONObject(i)["default_is_billable"].toString()
                val default_price_per_hour= response.getJSONObject(i)["default_price_per_hour"].toString()
                val account_id= response.getJSONObject(i)["account_id"].toString()



                val activite = Activite(null, idBexio,name, default_is_billable,default_price_per_hour,account_id)
                activiteListDatabase.add(activite)



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



        queueActivite.add(stringRequestActivite)
        numberOfRequestsToMake++



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlContact = "https://api.bexio.com/2.0/contact"
        val queueContact = Volley.newRequestQueue(this)
        val stringRequestContact = object : JsonArrayRequest(Method.GET, urlContact, JSONArray(), Response.Listener<JSONArray> { response ->



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



        queueContact.add(stringRequestContact)
        numberOfRequestsToMake++



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlProjet = "https://api.bexio.com/2.0/pr_project"
        val queueProjet = Volley.newRequestQueue(this)
        val stringRequestProjet = object : JsonArrayRequest(Method.GET, urlProjet,JSONArray(), Response.Listener<JSONArray> { response ->



            for (i in 0 until response.length()) {

                val idBexio= response.getJSONObject(i)["id"].toString()
                val nr= response.getJSONObject(i)["nr"].toString()
                val name= response.getJSONObject(i)["name"].toString()
                var startDate = response.getJSONObject(i)["start_date"].toString()
                var endDate = response.getJSONObject(i)["end_date"].toString()



                var ancienFormatdate = SimpleDateFormat("yyyy-MM-dd")
                var nouveauFormatDate = SimpleDateFormat("dd.MM.yy")
                var dStart = ancienFormatdate.parse(startDate)
                var changedDateStart = nouveauFormatDate.format(dStart)
                var changedDateEndFinal = ""



                if(endDate != "" && endDate != "null" && endDate != null){
                    var dEnd= ancienFormatdate.parse(endDate)
                    changedDateEndFinal = nouveauFormatDate.format(dEnd)
                }



                var comment= response.getJSONObject(i)["comment"].toString()
                if(comment == "" || comment == "null" || comment == null){
                    comment = ""
                }



//                val pr_state_id= response.getJSONObject(i)["pr_state_id"].toString().toInt()
//
//                var pr_project_type_id = response.getJSONObject(i)["pr_project_type_id"].toString().toInt()
//
//                val contact_id= response.getJSONObject(i)["contact_id"].toString().toInt()
//
//                val contact_sub_id= response.getJSONObject(i)["contact_sub_id"].toString().toInt()
//
//                val pr_invoice_type_id= response.getJSONObject(i)["pr_invoice_type_id"].toString().toInt()
//
//                val pr_invoice_type_amount= response.getJSONObject(i)["pr_invoice_type_amount"].toString()
//
//                val pr_budget_type_id= response.getJSONObject(i)["pr_budget_type_id"].toString().toInt()
//
//                val pr_budget_type_amount= response.getJSONObject(i)["pr_budget_type_amount"].toString()



                val projet = Projet(null, idBexio,nr, name,changedDateStart,changedDateEndFinal,comment,1,1, 1, 1,1,"200",1,"200")
                projetListDatabase.add(projet)



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



        queueProjet.add(stringRequestProjet)
        numberOfRequestsToMake++



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlTache = "https://api.bexio.com/2.0/task"
        val queueTache = Volley.newRequestQueue(this)
        val stringRequestTache = object : JsonArrayRequest(Method.GET, urlTache, JSONArray(), Response.Listener<JSONArray> { response ->



            for (i in 0 until response.length()) {

                val idBexio = response.getJSONObject(i)["id"].toString().toInt()
                val user_id= response.getJSONObject(i)["user_id"].toString().toInt()
                val finish_date= response.getJSONObject(i)["finish_date"].toString()
                val subject= response.getJSONObject(i)["subject"].toString()



//                val place= response.getJSONObject(i)["place"].toString().toInt()
//                val info= response.getJSONObject(i)["info"].toString()
//                val contact_id= response.getJSONObject(i)["contact_id"].toString().toInt()
//                val sub_contact_id= response.getJSONObject(i)["sub_contact_id"].toString().toInt()
//                val project_id= response.getJSONObject(i)["project_id"].toString().toInt()
//                val entry_id= response.getJSONObject(i)["entry_id"].toString().toInt()
//                val module_id= response.getJSONObject(i)["module_id"].toString().toInt()
//                val todo_status_id= response.getJSONObject(i)["todo_status_id"].toString().toInt()
//                val todo_priority_id= response.getJSONObject(i)["todo_priority_id"].toString().toInt()
//                val has_reminder= response.getJSONObject(i)["has_reminder"].toString().toBoolean()
//                val remember_type_id= response.getJSONObject(i)["remember_type_id"].toString().toInt()
//                val remember_time_id= response.getJSONObject(i)["remember_time_id"].toString().toInt()
//                val communication_kind_id= response.getJSONObject(i)["communication_kind_id"].toString().toInt()



                val taches = Tache(null,idBexio,user_id,finish_date,subject,1,"1",1,1,0,1,1,1,1,false,1,1,1)
                tacheListDatabase.add(taches)

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



        queueTache.add(stringRequestTache)
        numberOfRequestsToMake++



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlTemps = "https://api.bexio.com/2.0/timesheet"
        val queueTemps = Volley.newRequestQueue(this)
        val stringRequestTemps = object : JsonArrayRequest(Method.GET, urlTemps, JSONArray(), Response.Listener<JSONArray> { response ->



            // ------------------------------------------ Class Temps -----------------------------------------------

            for (i in 0 until response.length()) {


                val idBexio= response.getJSONObject(i)["id"].toString()
                val date= response.getJSONObject(i)["date"].toString()
                var duration = response.getJSONObject(i)["duration"].toString()
                if (duration.length == 4){ duration = "0$duration"}
                // Ajout du numéro de la semaine
                val dateConverter = SimpleDateFormat("yyyy-MM-dd").parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = dateConverter
                val semaine = calendar.get(Calendar.WEEK_OF_YEAR).toString()
                var text = response.getJSONObject(i)["text"].toString()



                // Création de la classe
                val temps = Temps(null, idBexio,date, duration, semaine, "2020",text)

                // Tri selon l'utilisateur
                if(response.getJSONObject(i)["user_id"] == 1){ tempsListDatabase.add(temps)}

            }

            // ------------------------------------------ Class Temps -----------------------------------------------



            // ------------------------------------------ Class Semaines -----------------------------------------------


//                    // Semaines
//                    val semaineDAO = database.semaineDAO
//                    semaineDAO.delete()
//
//
//
//                    // Temps
//                    val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
//                    val tempsDAO = database.tempsDAO
//                    val tempsList = tempsDAO.getItems() as ArrayList<Temps>
//
//
//
//                    for (i in 1..52) {
//
//
//
//                        // Heures totales
//                        var heuretotalesSecondes = 0.0
//                        for (temps in tempsList) {
//                            if (temps.semaine!!.toInt() == i){
//                                val timeString = temps.duration
//                                val factors = arrayOf(3600.0, 60.0, 1.0, 0.01)
//                                var value = 0.0
//                                timeString!!.replace(".", ":").split(":").forEachIndexed { i, s -> value += factors[i] * s.toDouble() }
//                                heuretotalesSecondes += value
//                            }
//                        }
//                        val tot_seconds = heuretotalesSecondes.toInt()
//                        val hours = tot_seconds / 3600
//                        val minutes = (tot_seconds % 3600) / 60
//                        val timeString = String.format("%02d:%02d", hours, minutes)
//
//
//
//                        // Date ddébut
//                        val sdf = SimpleDateFormat("dd.MM.yy")
//                        val cal = Calendar.getInstance()
//                        cal.set(Calendar.WEEK_OF_YEAR, i)
//                        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
//                        var dateDebut = sdf.format(cal.getTime())
//
//
//
//                        // Date fin
//                        val sdf2 = SimpleDateFormat("dd.MM.yy")
//                        val cal2 = Calendar.getInstance()
//                        cal2.set(Calendar.WEEK_OF_YEAR, i)
//                        cal2.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
//                        var dateFin = sdf2.format(cal2.getTime())
//
//
//
//                        val semaine = Semaines(null, "$i",dateDebut, dateFin, timeString)
//                        if (semaine.heuresTotales != "00:00"){ semaineDAO.insert(semaine)}
//
//
//
//                    }
//


            // ------------------------------------------ Class Semaines -----------------------------------------------



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



        queueTemps.add(stringRequestTemps)
        numberOfRequestsToMake++



        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        val urlUser = "https://api.bexio.com/3.0/users"
        val queueUser = Volley.newRequestQueue(this)
        val stringRequestUser = object : JsonArrayRequest(Method.GET, urlUser, JSONArray(), Response.Listener<JSONArray> { response ->



            for (i in 0 until response.length()) {
                val idBexio= response.getJSONObject(i)["id"].toString().toInt()
                val salutation_type= response.getJSONObject(i)["salutation_type"].toString()
                val firstname= response.getJSONObject(i)["firstname"].toString()
                val lastname= response.getJSONObject(i)["lastname"].toString()
                val email= response.getJSONObject(i)["email"].toString()



                var is_superadminCheck = false
                var is_accountantCheck = false
//                val is_superadmin= response.getJSONObject(i)["is_superadmin"].toString()
//                val is_accountant = response.getJSONObject(i)["is_accountant"].toString()
//                if (is_superadmin != ""){ is_superadminCheck = is_superadmin.toBoolean() }
//                if (is_accountant != ""){ is_accountantCheck = is_accountant.toBoolean() }



                val user = User(null, idBexio,salutation_type, firstname,lastname,email,is_superadminCheck,is_accountantCheck)
                userListDatabase.add(user)



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



        queueUser.add(stringRequestUser)
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



        // -----------------------------------------------------------------------------------------

        val activiteDAO = database.activiteDAO
        activiteDAO.delete()
        for (activite in activiteListDatabase){ activiteDAO.insert(activite)}



        val contactDAO = database.contactDAO
        contactDAO.delete()
        for (contact in contactListDatabase){ contactDAO.insert(contact)}



        val projetDAO = database.projetDAO
        projetDAO.delete()
        for (projet in projetListDatabase){ projetDAO.insert(projet)}



        val tacheDAO = database.tacheDAO
        tacheDAO.delete()
        for (tache in tacheListDatabase){tacheDAO.insert(tache)}



        val tempsDao = database.tempsDAO
        tempsDao.delete()
        for (temps in tempsListDatabase){tempsDao.insert(temps)}



        val userDAO = database.userDAO
        userDAO.delete()
        for (user in userListDatabase){userDAO.insert(user)}

        // -----------------------------------------------------------------------------------------



        val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentAct)



    }



}