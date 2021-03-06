package ch.creatis.bexio.First.Temps



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
import ch.creatis.bexio.FirstTachesTemps.Temps.First.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.FirstSecond.Temps.First.TempsActivityItemsNextSemaineDescription
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Semaines
import ch.creatis.bexio.Room.Temps
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_temps_first_items_semaine_b.*
import kotlinx.android.synthetic.main.activity_temps_first_items_semaine_a.view.*
import org.json.JSONArray
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TempsActivity : AppCompatActivity() {



        // -----------------------------------

        private var numberOfRequestsToMake = 0
        private var hasRequestFailed = false

        // -----------------------------------

        var semaineList = ArrayList<Semaines>()

        // -----------------------------------



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_temps_first_items_semaine_b)



            // Database
            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").fallbackToDestructiveMigration().allowMainThreadQueries().build()
            val semaineDAO = database.semaineDAO
            semaineList = semaineDAO.getItems() as ArrayList<Semaines>


            // Recycler View - Placer avant la requête
            recyclerViewTempsSemaine.layoutManager = LinearLayoutManager(this)
            recyclerViewTempsSemaine.adapter = TempsAdapter(semaineList, this)



            // Très important - Fait une requête
            RefreshRequest()



            // Swiper
            refreshViewTempsSemaine.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
            refreshViewTempsSemaine.setColorSchemeColors(Color.WHITE)
            refreshViewTempsSemaine.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



            // add Time Button
            addTimeButton.setOnClickListener {
                val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
                startActivity(intent)
            }



            // Selected Year View
            selectedYear.text = Calendar.getInstance().get(Calendar.YEAR).toString()



        }









        fun RefreshRequest(){



            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val tempsDAO = database.tempsDAO
            tempsDAO.delete()



            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")
            val companyUserIdDecode = sharedPreferences.getString("companyUserIdDecode", "")!!.toInt()

            // -----------------------------------------------------------------------------------------



            val url = "https://api.bexio.com/2.0/timesheet"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url, JSONArray(), Response.Listener<JSONArray> { response ->



                    // ------------------------------------------ Class Temps -----------------------------------------------

                    for (i in 0 until response.length()) {



                        val idBexio= response.getJSONObject(i)["id"].toString().toInt()

                        var userId = response.getJSONObject(i)["user_id"].toString().toInt()

                        var client_service_id = response.getJSONObject(i)["client_service_id"].toString().toInt()

                        var text = response.getJSONObject(i)["text"].toString()

                        var pr_project_id = response.getJSONObject(i)["pr_project_id"].toString()
                        if(pr_project_id != "null"){
                            pr_project_id.toInt()
                        } else{
                            pr_project_id = "0"
                        }

                        var duration = response.getJSONObject(i)["duration"].toString()
                        if (duration.length == 4){ duration = "0$duration"}



                        // ------------------------------ Date -------------------------------------

                        // Format d'entrée
                        var inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        // Format de sortie
                        var outputFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
                        // Converter
                        var convert = inputFormat.parse(response.getJSONObject(i)["date"].toString())



                        // Ajout de la date
                        var date = outputFormat.format(convert)



                        // Init de l'objet
                        val dateConverter = SimpleDateFormat("dd.MM.yyyy").parse(date)
                        val calendar = Calendar.getInstance()
                        calendar.time = dateConverter



                        // Ajout de la semaine
                        val semaine = calendar.get(Calendar.WEEK_OF_YEAR).toString()

                        // Ajout de l'année
                        val annee = calendar.get(Calendar.YEAR).toString()

                        // ------------------------------ Date -------------------------------------



                        // Création de la classe
                        val temps = Temps(null, idBexio, userId, client_service_id, text, pr_project_id.toInt(), date, duration, semaine, annee)



                        // Tri selon l'utilisateur
                        if(response.getJSONObject(i)["user_id"] == companyUserIdDecode){
                            tempsDAO.insert(temps)
                        }



                    }

                    // ------------------------------------------ Class Temps -----------------------------------------------



















                    // ------------------------------------------ Class Semaines -----------------------------------------------



                    // Semaines
                    val semaineDAO = database.semaineDAO
                    semaineDAO.delete()



                    // Temps
                    val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
                    val tempsDAO = database.tempsDAO
                    // Récupère l'année en cours
                    val date = Calendar.getInstance().get(Calendar.YEAR).toString()
                    val tempsList = tempsDAO.getItems(date) as ArrayList<Temps>



                    for (i in 1..52) {



                        // Heures totales
                        var heuretotalesSecondes = 0.0
                        for (temps in tempsList) {
                            if (temps.semaine!!.toInt() == i){
                                val timeString = temps.duration
                                val factors = arrayOf(3600.0, 60.0, 1.0, 0.01)
                                var value = 0.0
                                timeString!!.replace(".", ":").split(":").forEachIndexed { i, s -> value += factors[i] * s.toDouble() }
                                heuretotalesSecondes += value
                            }
                        }
                        val tot_seconds = heuretotalesSecondes.toInt()
                        val hours = tot_seconds / 3600
                        val minutes = (tot_seconds % 3600) / 60
                        val timeString = String.format("%02d:%02d", hours, minutes)



                        // Date ddébut
                        val sdf = SimpleDateFormat("dd.MM")
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.WEEK_OF_YEAR, i)
                        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                        var dateDebut = sdf.format(cal.getTime())



                        // Date fin
                        val sdf2 = SimpleDateFormat("dd.MM.yy")
                        val cal2 = Calendar.getInstance()
                        cal2.set(Calendar.WEEK_OF_YEAR, i)
                        cal2.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                        var dateFin = sdf2.format(cal2.getTime())



                        val semaine = Semaines(null, "$i",dateDebut, dateFin, timeString)
                        if (semaine.heuresTotales != "00:00"){ semaineDAO.insert(semaine)}



                    }



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
            val semaineDAO = database.semaineDAO
            semaineList = semaineDAO.getItems() as ArrayList<Semaines>
            refreshViewTempsSemaine.isRefreshing = false



            // Très important - Rafraîchit la tableView après la requête
            recyclerViewTempsSemaine.layoutManager = LinearLayoutManager(this)
            recyclerViewTempsSemaine.adapter =
                TempsAdapter(semaineList, this)
        }

    }



    fun Alerte(){
        refreshViewTempsSemaine.isRefreshing = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aucune connexion à internet")
        builder.setMessage("Vérifiez vos réglages avant de pouvoir utiliser l'application.")
        builder.setPositiveButton("Ok"){dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // -------------------------------------------------------------------------------------- Internet ---------------------------------------------------------------------------------------



}



class TempsAdapter(val items : ArrayList<Semaines>, val context: Context) : RecyclerView.Adapter<TempsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempsHolder {
        return TempsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.activity_temps_first_items_semaine_a,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: TempsHolder, position: Int) {



//        val dateConverter = SimpleDateFormat("yyyy-MM-dd").parse(items[position].date)
//        val calendar = Calendar.getInstance()
//        calendar.time = dateConverter
//        var firstDay = calendar.get(calendar.firstDayOfWeek)



//        //-------------------------------
//
//        val time = items[position].duration
//        val sdf = SimpleDateFormat("HH:mm")
//        val date = sdf.parse(time)
//
//        //-------------------------------



        holder.viewDate?.text = items[position].numeroSemaine
        holder.viewFirstDay.text = items[position].dateDebut
        holder.viewLastDay.text = items[position].dateFin
        holder.viewDuration?.text = items[position].heuresTotales.toString()


        holder.semaineView.setOnClickListener {

            val intent = Intent(context, TempsActivityItemsNextSemaineDescription::class.java)
            intent.putExtra("numeroSemaine", items[position].numeroSemaine)
            context!!.startActivity(intent)

        }



    }



    override fun getItemCount(): Int {
        return items.size
    }



}



class TempsHolder (view: View) : RecyclerView.ViewHolder(view) {

    val semaineView = view.semaineView
    val viewDate = view.semaineNbrLabel
    val viewFirstDay = view.firstDayLabel
    val viewLastDay = view.lastDayLabel
    val viewDuration = view.durationLabel

}