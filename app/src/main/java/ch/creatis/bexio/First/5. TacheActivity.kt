package ch.creatis.bexio.First



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
import ch.creatis.bexio.R
import ch.creatis.bexio.TachesTemps.Taches.ProjetsActivityNextSaisieTaches
import ch.creatis.bexio.FirstSecond.TachesActivityNext
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Tache
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_taches.*
import kotlinx.android.synthetic.main.activity_taches_items.view.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tachesDAO = database.tacheDAO
        tachesList = tachesDAO.getItems() as ArrayList<Tache>



        // Recycler View - Placer avant la requête
        recyclerViewTaches.layoutManager = LinearLayoutManager(this)
        recyclerViewTaches.adapter = TachesAdapter(tachesList, this)



        // Très important - Fait une requête
        RefreshRequest()



        // RefreshView
        refreshViewTaches.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        refreshViewTaches.setColorSchemeColors(Color.WHITE)
        // Met à jour la base de donnée quand l'utilisateur Swipe !
        refreshViewTaches.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



        // Add Task
        addTaskButton.setOnClickListener {
            val intent = Intent(this, ProjetsActivityNextSaisieTaches::class.java)
            startActivity(intent)
        }



    }










    fun RefreshRequest(){



        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tachesDAO = database.tacheDAO
        tachesDAO.delete()



        // -----------------------------------------------------------------------------------------

        val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

        // -----------------------------------------------------------------------------------------



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



                val contact_idT = response.getJSONObject(i)["contact_id"].toString()
                var contact_id = 0
                if (contact_idT != "null"){ contact_id = response.getJSONObject(i)["contact_id"].toString().toInt() }



//                val sub_contact_id= response.getJSONObject(i)["sub_contact_id"].toString().toInt()
                    val project_idT = response.getJSONObject(i)["project_id"].toString()
                    var project_id = 0
                    if (project_idT != "null"){ project_id = response.getJSONObject(i)["project_id"].toString().toInt() }
//                val entry_id= response.getJSONObject(i)["entry_id"].toString().toInt()
//                val module_id= response.getJSONObject(i)["module_id"].toString().toInt()
                val todo_status_id= response.getJSONObject(i)["todo_status_id"].toString().toInt()
//                val todo_priority_id= response.getJSONObject(i)["todo_priority_id"].toString().toInt()
//                val has_reminder= response.getJSONObject(i)["has_reminder"].toString().toBoolean()
//                val remember_type_id= response.getJSONObject(i)["remember_type_id"].toString().toInt()
//                val remember_time_id= response.getJSONObject(i)["remember_time_id"].toString().toInt()
//                val communication_kind_id= response.getJSONObject(i)["communication_kind_id"].toString().toInt()



                    val taches = Tache(null,idBexio,user_id,finish_date,subject,0,"string",contact_id,0,project_id,0,0,todo_status_id,0,false,0,0,0)
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



        queueTache.add(stringRequestTache)
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
            val tachesDAO = database.tacheDAO
            tachesList = tachesDAO.getItems() as ArrayList<Tache>
            refreshViewTaches.isRefreshing = false



            // Très important - Rafraîchit la tableView après la requête
            recyclerViewTaches.layoutManager = LinearLayoutManager(this)
            recyclerViewTaches.adapter = TachesAdapter(tachesList, this)
        }

    }



    fun Alerte(){
        refreshViewTaches.isRefreshing = false
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



        println(items[position].finish_date)



        // Sujet
        holder.viewObjet.text = items[position].subject



//        Statut - 2020-04-29 13:15:00
//        int compare = date1.compareTo(date2);
//        compare > 0, if date2 is greater than date1
//        compare < 0, if date2 is smaller than date1
//        compare = 0, if date1 is equal to date2



        // Statut - Le formatage de la date
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val currentDate = sdf.format(Date())



        // Statut - Les images des statuts avec la comparaison de la date
        if(items[position].todo_status_id == 1 && items[position].finish_date!!.isNullOrEmpty()){
            holder.viewStatus.text = "En suspens"
            holder.viewStatus.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        }

        else if (items[position].todo_status_id == 1 && items[position].finish_date!!.compareTo(currentDate) < 0){
            holder.viewStatus.text = "En retard"
            holder.viewStatus.setBackgroundResource(R.drawable.taches_activity_items_status_en_retard)
        }

        else if (items[position].todo_status_id == 1 && items[position].finish_date!!.compareTo(currentDate) > 0){
            holder.viewStatus.text = "En suspens"
            holder.viewStatus.setBackgroundResource(R.drawable.taches_activity_items_status_en_suspens)
        }

        else if (items[position].todo_status_id == 5){
            holder.viewStatus.text = "Terminé"
            holder.viewStatus.setBackgroundResource(R.drawable.taches_activity_items_status_termine)
        }



        // Intent
        var tache= items[position]
        holder.tacheView.setOnClickListener {
            val intent = Intent(context, TachesActivityNext::class.java)
            intent.putExtra("idBexio", tache.idBexio)
            intent.putExtra("project_id", tache.project_id)
            intent.putExtra("user_id", tache.user_id)
            intent.putExtra("finish_date", tache.finish_date)
            intent.putExtra("subject", tache.subject)
            intent.putExtra("contact_id", tache.contact_id)
            intent.putExtra("todo_status_id", tache.todo_status_id)
            context.startActivity(intent)
        }



    }



    override fun getItemCount(): Int {
        return items.size
    }



}



class TachesHolder (view: View) : RecyclerView.ViewHolder(view) {

    val tacheView = view.tacheView
    val viewObjet = view.objetLabel
    val viewStatus = view.tachesStatutLabel


}