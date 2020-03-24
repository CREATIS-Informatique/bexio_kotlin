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
import ch.creatis.bexio.Next.All.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.Next.TempsActivityItemsNextSemaineDescription
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Semaines
import ch.creatis.bexio.Room.Temps
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_temps_items_semaine_a.*
import kotlinx.android.synthetic.main.activity_temps_items_semaine.view.*
import org.json.JSONArray
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
            setContentView(R.layout.activity_temps_items_semaine_a)



            // Database
            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val semaineDAO = database.semaineDAO
            semaineList = semaineDAO.getItems() as ArrayList<Semaines>



            // Adapter
            RefreshRequest()
            recyclerViewTemps.layoutManager = LinearLayoutManager(this)
            recyclerViewTemps.adapter = TempsAdapter(semaineList, this)



            // RefreshView
            refreshViewTemps.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
            refreshViewTemps.setColorSchemeColors(Color.WHITE)
            refreshViewTemps.setOnRefreshListener { if(numberOfRequestsToMake == 0){ if (isConnected()) {RefreshRequest()} else { Alerte() } } }



            // addTimeButton
            addTimeButton.setOnClickListener {
                val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
                startActivity(intent)
            }



        }









        fun RefreshRequest(){



            val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
            val tempsDAO = database.tempsDAO
            tempsDAO.delete()



            // -----------------------------------------------------------------------------------------

            val sharedPreferences = this.getSharedPreferences("Bexio", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("ACCESSTOKEN", "")

            // -----------------------------------------------------------------------------------------


            val url = "https://api.bexio.com/2.0/timesheet"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : JsonArrayRequest(Method.GET, url, JSONArray(), Response.Listener<JSONArray> { response ->





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
                        val temps = Temps(null, idBexio,date, duration, semaine,text)



                        // Tri selon l'utilisateur
                        if(response.getJSONObject(i)["user_id"] == 1){
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
                    val tempsList = tempsDAO.getItems() as ArrayList<Temps>



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
                        val sdf = SimpleDateFormat("dd.MM.yy")
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



class TempsAdapter(val items : ArrayList<Semaines>, val context: Context) : RecyclerView.Adapter<TempsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempsHolder {
        return TempsHolder(LayoutInflater.from(context).inflate(R.layout.activity_temps_items_semaine, parent, false))
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