package ch.creatis.bexio.First



import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
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
import ch.creatis.bexio.FirstSecond.ProjetsActivityNext
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Projet
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_projets.*
import kotlinx.android.synthetic.main.activity_projets_items.view.*
import org.json.JSONArray
import java.text.SimpleDateFormat
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



        // Recycler View - Placer avant la requête
        recyclerViewProjets.layoutManager = LinearLayoutManager(this)
        recyclerViewProjets.adapter = ProjetsAdapter(projectList, this)



        // Très important - Fait une requête
        RefreshRequest()



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
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------



            val url = "https://api.bexio.com/2.0/pr_project"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url,JSONArray(), Response.Listener<JSONArray> { response ->



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



               val pr_state_id= response.getJSONObject(i)["pr_state_id"].toString().toInt()

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



                    val projet = Projet(null, idBexio,nr, name,changedDateStart,changedDateEndFinal,comment,pr_state_id,1, 1, 1,1,"200",1,"200")
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



            // Très important - Rafraîchit la tableView après la requête
            recyclerViewProjets.layoutManager = LinearLayoutManager(this)
            recyclerViewProjets.adapter = ProjetsAdapter(projectList, this)
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



        val projet= items[position]
        holder.projetsView.setOnClickListener {
            val intent = Intent(context, ProjetsActivityNext::class.java)
            intent.putExtra("name", projet.name)
            intent.putExtra("start_date", projet.start_date)
            intent.putExtra("end_date", projet.end_date)
            intent.putExtra("comment", projet.comment)
            intent.putExtra("nr", projet.nr)
            intent.putExtra("pr_state_id", projet.pr_state_id)
            context!!.startActivity(intent)
        }



        holder.projectLabel?.text = items[position].name
        holder.startDate?.text = "Début: " + items[position].start_date



        // Statut
        println(items[position].pr_state_id)
        if(items[position].pr_state_id == 1){
            holder.projetStatusLabel.text = "Ouvert"
            holder.projetStatusLabel.setBackgroundResource(R.drawable.projets_activity_items_status_en_suspens)
        } else if (items[position].pr_state_id == 2){
            holder.projetStatusLabel.text = "Actif"
            holder.projetStatusLabel.setBackgroundResource(R.drawable.projets_activity_items_status_actif)
        } else if (items[position].pr_state_id == 3){
            holder.projetStatusLabel.text = "Archivé"
            holder.projetStatusLabel.setBackgroundResource(R.drawable.projets_activity_items_status_archive)
        }



    }



    override fun getItemCount(): Int {
        return items.size
    }



}







class ProjetsHolder (view: View) : RecyclerView.ViewHolder(view) {

    val projetsView = view.projetsView

    val startDate = view.startDateLabel
    val projetStatusLabel = view.projetStatusLabel
    val projectLabel = view.projetLabel


}