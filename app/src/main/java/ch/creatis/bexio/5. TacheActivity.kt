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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Tache
import ch.creatis.bexio.Room.Temps
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_taches.*
import kotlinx.android.synthetic.main.activity_taches_items.view.*
import kotlinx.android.synthetic.main.activity_temps.*
import org.json.JSONArray


class TacheActivity : AppCompatActivity() {



    // -----------------------------------

    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false

    // -----------------------------------

    var tachesList = ArrayList<Tache>()

    // -----------------------------------



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taches)



        recyclerViewTaches.layoutManager = LinearLayoutManager(this)
        recyclerViewTaches.adapter = TachesAdapter(tachesList, this)



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tachesDAO = database.tempsDAO
        tachesList = tachesDAO.getItems() as ArrayList<Tache>



        // Adapter
        recyclerViewTaches.layoutManager = LinearLayoutManager(this)
        recyclerViewTaches.adapter = TachesAdapter(tachesList, this)
        RefreshRequest()



        // RefreshView
        refreshViewTaches.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        refreshViewTaches.setColorSchemeColors(Color.WHITE)
        refreshViewTaches.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



    }










    fun RefreshRequest(){



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tachesDAO = database.tacheDAO
        tachesDAO.delete()



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val url = "https://api.bexio.com/2.0/timesheet"
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonArrayRequest(Method.GET, url,
            JSONArray(), Response.Listener<JSONArray> { response ->



                println(response)



                for (i in 0 until response.length()) {

                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name_un= response.getJSONObject(i)["duration"].toString()
                    val taches = Tache(null, idBexio,name_un)
                    tachesDAO.insert(taches)

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














    // -------------------------------------------------------------------------------------- Internet --------------------------------------------------------------------------------------

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
            val tachesDAO = database.tempsDAO
            tachesList = tachesDAO.getItems() as ArrayList<Tache>
            refreshViewTemps.isRefreshing = false
        }

    }



    fun Alerte(){
        refreshViewTemps.isRefreshing = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aucune connexion à internet")
        builder.setMessage("Vérifiez vos réglages avant de pouvoir utiliser l'application.")
        builder.setPositiveButton("Ok"){dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------







}







class TachesAdapter(val items : ArrayList<Tache>, val context: Context) : RecyclerView.Adapter<TachesHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesHolder {
        return TachesHolder(LayoutInflater.from(context).inflate(R.layout.activity_taches_items, parent, false))
    }



    override fun onBindViewHolder(holder: TachesHolder, position: Int) {
//        holder.viewTaches.text = items.get(position)
    }



    override fun getItemCount(): Int {
        return items.size
    }



}











class TachesHolder (view: View) : RecyclerView.ViewHolder(view) {

    val viewTaches = view.dateLimiteLabel

}