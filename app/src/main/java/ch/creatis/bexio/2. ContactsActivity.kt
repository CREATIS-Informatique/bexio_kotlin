package ch.creatis.bexio



import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.room.*
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Contact
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_contacts_items.view.*
import org.json.JSONArray
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



class ContactsActivity : AppCompatActivity() {







        // -----------------------------------

        private var numberOfRequestsToMake = 0
        private var hasRequestFailed = false

        // -----------------------------------



        var adapter: ContactAdapter? = null
        var contactList = ArrayList<Contact>()







        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_contacts)



            // Database
            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val contactDAO = database.contactDAO
            contactList = contactDAO.getItems() as ArrayList<Contact>



            // Adapter
            adapter = ContactAdapter(this@ContactsActivity, contactList)
            GridContacts.adapter = adapter



            // RefreshView
            refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
            refreshView.setColorSchemeColors(Color.WHITE)
            refreshView.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



        }







        fun RefreshRequest(){

            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val contactDAO = database.contactDAO
            contactDAO.delete()

            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val org = sharedPreferences.getString("ORG", "")
            val url = "https://office.bexio.com/api2.php/$org/contact"
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url,JSONArray(), Response.Listener<JSONArray> { response ->

                for (i in 0 until response.length()) {
                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name= response.getJSONObject(i)["name_1"].toString()
                    val address= response.getJSONObject(i)["address"].toString()
                    val postcode= response.getJSONObject(i)["postcode"].toString()
                    val city= response.getJSONObject(i)["city"].toString()
                    val mail= response.getJSONObject(i)["mail"].toString()
                    val phone_fixed= response.getJSONObject(i)["phone_fixed"].toString()
                    val contact = Contact(null, idBexio,name,address,postcode,city,mail,phone_fixed)
                    contactDAO.insert(contact)
                }



                adapter = ContactAdapter(this@ContactsActivity, contactList)
                GridContacts.adapter = adapter



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
                adapter = ContactAdapter(this@ContactsActivity, contactList)
                GridContacts.adapter = adapter
                refreshView.isRefreshing = false
            }

        }



        fun Alerte(){

            refreshView.isRefreshing = false
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Aucune connexion à internet")
            builder.setMessage("Vérifiez vos réglages avant de pouvoir utiliser l'application.")
            builder.setPositiveButton("Ok"){dialog, which -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------







    }







    class ContactAdapter : BaseAdapter {

        var contactsList = ArrayList<Contact>()
        var context: Context? = null



        constructor(context: Context, contactsList: ArrayList<Contact>) : super() {
            this.context = context
            this.contactsList = contactsList
        }



        override fun getCount(): Int { return contactsList.size }


        override fun getItem(position: Int): Any { return contactsList[position] }


        override fun getItemId(position: Int): Long { return position.toLong() }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val contact = this.contactsList[position]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var contactView = inflator.inflate(R.layout.activity_contacts_items, null)
            //            foodView.photoContacts.setImageResource(contact.id!!)
            contactView.tvName.text = contact.name!!
            return contactView



        }



}

