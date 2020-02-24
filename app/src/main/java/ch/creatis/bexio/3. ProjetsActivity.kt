package ch.creatis.bexio



import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import ch.creatis.bexio.Next.ContactsActivityNext
import ch.creatis.bexio.Next.ProjetsActivityNext
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Projet
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_projets.*
import kotlinx.android.synthetic.main.activity_projets_items.view.*
import org.json.JSONArray
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProjetsActivity : AppCompatActivity() {



    // -----------------------------------

    private var numberOfRequestsToMake = 0
    private var hasRequestFailed = false

    // -----------------------------------

    var projectList = ArrayList<Projet>()

    // -----------------------------------



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets)



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val projetDAO = database.projetDAO
        projectList = projetDAO.getItems() as ArrayList<Projet>



        // Adapter
        RefreshRequest()
        recyclerViewProjets.layoutManager = LinearLayoutManager(this)
        recyclerViewProjets.adapter = ProjetsAdapter(projectList, this)



        // RefreshView
        refreshViewProjets.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        refreshViewProjets.setColorSchemeColors(Color.WHITE)
        refreshViewProjets.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



    }



    fun RefreshRequest(){



            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val projetDAO = database.projetDAO
            projetDAO.delete()



            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val url = "https://api.bexio.com/2.0/pr_project"
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url,JSONArray(), Response.Listener<JSONArray> { response ->

                println(response)

                for (i in 0 until response.length()) {

                    val idBexio= response.getJSONObject(i)["id"].toString()
                    val name_un= response.getJSONObject(i)["nr"].toString()
                    val name_deux= response.getJSONObject(i)["name"].toString()
                    val address= response.getJSONObject(i)["start_date"].toString()
                    val postcode= response.getJSONObject(i)["pr_state_id"].toString()
                    val city= response.getJSONObject(i)["comment"].toString()
                    val projet = Projet(null, idBexio,name_un, name_deux,address,postcode,city)
                    projetDAO.insert(projet)

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
            val projectDAO = database.projetDAO
            projectList = projectDAO.getItems() as ArrayList<Projet>
            refreshViewProjets.isRefreshing = false
        }

    }



    fun Alerte(){

        refreshViewProjets.isRefreshing = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aucune connexion à internet")
        builder.setMessage("Vérifiez vos réglages avant de pouvoir utiliser l'application.")
        builder.setPositiveButton("Ok"){dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------



}






class ProjetsAdapter(val items : ArrayList<Projet>, val context: Context) : RecyclerView.Adapter<ProjetsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjetsHolder {
        return ProjetsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.activity_projets_items,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ProjetsHolder, position: Int) {

        holder.projectLabel?.text = items[position].name
        holder.nr?.text = "Nº " + items[position].nr
        holder.startDate?.text = items[position].start_date

        if (items[position].pr_state_id == "1"){
            holder.endDate?.text = "Actif"
        } else if(items[position].pr_state_id == "2"){
            holder.endDate?.text = "Ouvert"
//            holder.endDate.setBackgroundColor(R.color)
        }


        holder.projetsView.setOnClickListener {

            val intent = Intent(context, ProjetsActivityNext::class.java)
            context!!.startActivity(intent)

        }



    }



    override fun getItemCount(): Int {
        return items.size
    }



}







class ProjetsHolder (view: View) : RecyclerView.ViewHolder(view) {

    val projetsView = view.projetsView
    val projectLabel = view.projetLabel
    val nr = view.nrLabel
    val startDate = view.startDateLabel
    val endDate = view.endDateLabel

}