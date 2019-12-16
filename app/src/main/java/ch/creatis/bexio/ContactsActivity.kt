package ch.creatis.bexio



import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_contacts_items.view.*
import org.json.JSONArray
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ContactsActivity : AppCompatActivity() {



    var adapter: ContactAdapter? = null
    var contactList = ArrayList<Contact>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)



        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var org = sharedPreferences.getString("ORG", "")
        var accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
        val queue = Volley.newRequestQueue(this)
        val url = "https://office.bexio.com/api2.php/$org/contact"
        val stringRequest = object : JsonArrayRequest(Method.GET, url,JSONArray(), Response.Listener<JSONArray> { response ->



            for (i in 0 until response.length()) {
                var id= response.getJSONObject(i)["id"].toString()
                var name= response.getJSONObject(i)["name_1"].toString()
                var address= response.getJSONObject(i)["address"].toString()
                var postcode= response.getJSONObject(i)["postcode"].toString()
                var city= response.getJSONObject(i)["city"].toString()
                var mail= response.getJSONObject(i)["mail"].toString()
                var phone_fixed= response.getJSONObject(i)["phone_fixed"].toString()
                val contact = Contact(id,name,address,postcode,city,mail,phone_fixed)
                contactList.add(contact)
            }

            adapter = ContactAdapter(this@ContactsActivity, contactList)
            GridContacts.adapter = adapter



        }, Response.ErrorListener {})

        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $accessToken"
                return headers
            }
        }

        queue.add(stringRequest)



    }



}



    // -------------------------------------------------------------------------------------------------



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



    // ---------------------------------------------------------------------------------------------



    class Contact {



        var id: String? = null
        var name: String? = null
        var address: String? = null
        var postcode: String? = null
        var city: String? = null
        var mail: String? = null
        var phone_fixed: String? = null



        constructor(id: String, name: String, address: String, postcode: String, city: String, mail: String, phone_fixed: String) {
            this.id = id
            this.name = name
            this.address = address
            this.postcode = postcode
            this.city = city
            this.mail = mail
            this.phone_fixed = phone_fixed
        }



}