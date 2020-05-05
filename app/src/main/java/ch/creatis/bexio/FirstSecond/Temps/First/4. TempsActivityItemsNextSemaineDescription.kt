package ch.creatis.bexio.FirstSecond.Temps.First



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ch.creatis.bexio.FirstSecond.Temps.FirstSecond.TempsActivityItemsNextJourDescription
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Temps
import kotlinx.android.synthetic.main.activity_temps_second_items_next_jour_a.view.*
import kotlinx.android.synthetic.main.activity_temps_second_items_next_jour_b.*



class TempsActivityItemsNextSemaineDescription : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps_second_items_next_jour_b)



        var numeroSemaine = intent.getStringExtra("numeroSemaine")



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tempsDAO = database.tempsDAO
        var tempsList = tempsDAO.getTempsByNumeroSemaine(numeroSemaine) as ArrayList<Temps>



        // Swiper
        refreshViewTempsJour.setRefreshing(false)
        refreshViewTempsJour.setEnabled(false)



        // Recycler View
        recyclerViewTempsJour.layoutManager = LinearLayoutManager(this)
        recyclerViewTempsJour.adapter = TempsAdapterNext(tempsList, this)



    }



}



class TempsAdapterNext(val items : ArrayList<Temps>, val context: Context) : RecyclerView.Adapter<TempsHolderNext>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempsHolderNext {
        return TempsHolderNext(LayoutInflater.from(context).inflate(R.layout.activity_temps_second_items_next_jour_a, parent, false))
    }



    override fun onBindViewHolder(holder: TempsHolderNext, position: Int) {
        holder.semaineNbrLabel.text = items[position].semaine
        holder.firstDayLabel.text = items[position].date
        holder.durationLabel.text = items[position].duration



        val temps= items[position]
        holder.jourTempsView.setOnClickListener {
            val intent = Intent(context, TempsActivityItemsNextJourDescription::class.java)
            intent.putExtra("idBexio", temps.idBexio)
            intent.putExtra("userId", temps.userId)
            intent.putExtra("client_service_id", temps.client_service_id)
            intent.putExtra("text", temps.text)
            intent.putExtra("pr_project_id", temps.pr_project_id)
            intent.putExtra("date", temps.date)
            intent.putExtra("duration", temps.duration)
            intent.putExtra("semaine", temps.semaine)
            intent.putExtra("annee", temps.annee)
            context.startActivity(intent)
        }



    }



    override fun getItemCount(): Int {
        return items.size
    }



}



class TempsHolderNext (view: View) : RecyclerView.ViewHolder(view) {

        val semaineNbrLabel = view.semaineNbrLabel
        val firstDayLabel = view.firstDayLabel
        val durationLabel = view.durationLabel
        val jourTempsView = view.jourTempsView

}