package ch.creatis.bexio



import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import ch.creatis.bexio.Next.ContactsActivityNext
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Contact
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_contacts_items.view.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



class ContactsActivity : AppCompatActivity() {


    // -----------------------------------

    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false

    // -----------------------------------

    var contactList = ArrayList<Contact>()

    // -----------------------------------



        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val contactDAO = database.contactDAO
        contactList = contactDAO.getItems() as ArrayList<Contact>



        // Adapter
        RefreshRequest()
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        recyclerViewContacts.adapter = ContactsAdapter(contactList, this)



        // RefreshView
        refreshViewContacts.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        refreshViewContacts.setColorSchemeColors(Color.WHITE)
        refreshViewContacts.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



            // EditText


    }




            fun RefreshRequest(){

            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val contactDAO = database.contactDAO
            contactDAO.delete()



            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val url = "https://api.bexio.com/2.0/contact"
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url,JSONArray(), Response.Listener<JSONArray> { response ->


                for (i in 0 until response.length()) {
                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name_un= response.getJSONObject(i)["name_1"].toString()
                    val name_deux= response.getJSONObject(i)["name_2"].toString()
                    val address= response.getJSONObject(i)["address"].toString()
                    val postcode= response.getJSONObject(i)["postcode"].toString()
                    val city= response.getJSONObject(i)["city"].toString()
                    val mail= response.getJSONObject(i)["mail"].toString()
                    val mail_second= response.getJSONObject(i)["mail_second"].toString()
                    val phone_fixed= response.getJSONObject(i)["phone_fixed"].toString()
                    val phone_fixed_second= response.getJSONObject(i)["phone_fixed_second"].toString()
                    val phone_mobile= response.getJSONObject(i)["phone_mobile"].toString()
                    val fax= response.getJSONObject(i)["fax"].toString()
                    val url= response.getJSONObject(i)["url"].toString()
                    val skype_name= response.getJSONObject(i)["skype_name"].toString()
                    var country_id = response.getJSONObject(i)["country_id"].toString()






                    // -----------------------------------------------------   Country    -------------------------------------------------------

                    if(country_id != "" && country_id != "null"){
                        val urlCountry = "https://api.bexio.com/2.0/country/$country_id"
                        val queueCountry = Volley.newRequestQueue(this)
                        val stringRequestCountry = object: StringRequest(Method.GET, urlCountry, Response.Listener<String> { response ->

                            var jsonObject = JSONObject(response)
                            country_id= jsonObject.optString("name", "")

                            val contact = Contact(null, idBexio,name_un, name_deux,address,postcode,city,country_id,mail,mail_second,phone_fixed,phone_fixed_second,phone_mobile,fax,url,skype_name)
                            contactDAO.insert(contact)

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

                        queueCountry.add(stringRequestCountry)
                        numberOfRequestsToMake++

                    }

                    // -----------------------------------------------------   Country    -------------------------------------------------------







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







        // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------

        fun isConnected(): Boolean {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }



        fun requestEndInternet() {
            if (hasRequestFailed) {
                hasRequestFailed = false

            } else {
                val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
                val contactDAO = database.contactDAO
                contactList = contactDAO.getItems() as ArrayList<Contact>
                refreshViewContacts.isRefreshing = false
            }

        }



        fun Alerte(){

            refreshViewContacts.isRefreshing = false
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Aucune connexion à internet")
            builder.setMessage("Vérifiez vos réglages avant de pouvoir utiliser l'application.")
            builder.setPositiveButton("Ok"){dialog, which -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------



}





class ContactsAdapter(val items : ArrayList<Contact>, val context: Context) : RecyclerView.Adapter<ContactsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        return ContactsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.activity_contacts_items,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {

        val contact= items[position]

        holder.contactName.text = contact.name_un + " " +contact.name_deux!!

        holder.contactView.setOnClickListener {

                val intent = Intent(context, ContactsActivityNext::class.java)
                intent.putExtra("name_un", contact.name_un)
                intent.putExtra("name_deux", contact.name_deux)
                intent.putExtra("address", contact.address)
                intent.putExtra("postcode", contact.postcode)
                intent.putExtra("city", contact.city)
                intent.putExtra("country_id", contact.country_id)
                intent.putExtra("mail", contact.mail)
                intent.putExtra("mail_second", contact.mail_second)
                intent.putExtra("phone_fixed", contact.phone_fixed)
                intent.putExtra("phone_fixed_second", contact.phone_fixed_second)
                intent.putExtra("phone_mobile", contact.phone_mobile)
                intent.putExtra("fax", contact.fax)
                intent.putExtra("url", contact.url)
                intent.putExtra("skype_name", contact.skype_name)
                context!!.startActivity(intent)

        }

        holder.mailButton.setOnClickListener {

            if (contact.mail == "" || contact.mail == "null") { Toast.makeText(context, "Il n'y pas de mail !", Toast.LENGTH_LONG).show() }else {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.type = "plain/text"
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.mail))
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                        context!!.startActivity(Intent.createChooser(emailIntent, "Choisissez une application Mail"))
                    }
        }



        holder.telButton.setOnClickListener {
            if (contact.phone_fixed == "" || contact.phone_fixed == "null") { Toast.makeText(context, "Il n'y pas de téléphone !", Toast.LENGTH_LONG).show()} else {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${contact.phone_fixed}")
                        context!!.startActivity(intent)
                    }

        }



    }



    override fun getItemCount(): Int {
        return items.size
    }




}







class ContactsHolder (view: View) : RecyclerView.ViewHolder(view) {

        val contactView = view.contactView
        val contactName = view.contactName
        val mailButton = view.mailButton
        val telButton = view.telButton

}