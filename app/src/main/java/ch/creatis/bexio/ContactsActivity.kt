package ch.creatis.bexio



import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts_items.view.*



class ContactsActivity : AppCompatActivity() {



    var adapter: FoodAdapter? = null
    var foodsList = ArrayList<Food>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)



//        // load foods
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta",R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        foodsList.add(Food("Theodros Mulugeta", R.drawable.contacts_photo_profil))
//        adapter = FoodAdapter(this, foodsList)
//        gvFoods.adapter = adapter



        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var org = sharedPreferences.getString("ORG", "")
        var accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
        val queue = Volley.newRequestQueue(this)
        val url = "https://office.bexio.com/api2.php/$org/task"
        val stringRequest = object : StringRequest(Method.GET, url, Response.Listener<String> { response ->



            // -------------------------------------------------------------------------------------

            println(response)

            // -------------------------------------------------------------------------------------



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



// Contact Adapter

class FoodAdapter : BaseAdapter {
    var foodsList = ArrayList<Food>()
    var context: Context? = null

    constructor(context: Context, foodsList: ArrayList<Food>) : super() {
        this.context = context
        this.foodsList = foodsList
    }

    override fun getCount(): Int {
        return foodsList.size
    }

    override fun getItem(position: Int): Any {
        return foodsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val food = this.foodsList[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var foodView = inflator.inflate(R.layout.activity_contacts_items, null)
        foodView.photoContacts.setImageResource(food.image!!)
        foodView.tvName.text = food.name!!

        return foodView
    }
}



// -------------------------------------------------------------------------------------------------



// Contacts class

class Food {



    var name: String? = null
    var image: Int? = null



    constructor(name: String, image: Int) {
        this.name = name
        this.image = image
    }



}