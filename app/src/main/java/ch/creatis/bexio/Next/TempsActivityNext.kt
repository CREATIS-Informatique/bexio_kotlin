package ch.creatis.bexio.Next



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ch.creatis.bexio.Next.All.ProjetsActivityNextSaisieTaches
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Semaines
import ch.creatis.bexio.Room.Temps
import ch.creatis.bexio.TempsAdapter
import kotlinx.android.synthetic.main.activity_temps_items_next_jour.view.*
import kotlinx.android.synthetic.main.activity_temps_items_next_jour_a.*


class TempsActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps_items_next_jour_a)



        var numeroSemaine = intent.getStringExtra("numeroSemaine")



        // Database
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "mydb").allowMainThreadQueries().build()
        val tempsDAO = database.tempsDAO
        var tempsList = tempsDAO.getTempsByNumeroSemaine(numeroSemaine) as ArrayList<Temps>



        recyclerViewTempsJour.layoutManager = LinearLayoutManager(this)
        recyclerViewTempsJour.adapter = TempsAdapterNext(tempsList, this)



    }



}



class TempsAdapterNext(val items : ArrayList<Temps>, val context: Context) : RecyclerView.Adapter<TempsHolderNext>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempsHolderNext {
        return TempsHolderNext(LayoutInflater.from(context).inflate(R.layout.activity_temps_items_next_jour, parent, false))
    }



    override fun onBindViewHolder(holder: TempsHolderNext, position: Int) {
        holder.semaineNbrLabel.text = items[position].semaine
        holder.firstDayLabel.text = items[position].date
        holder.durationLabel.text = items[position].duration



        holder.jourTempsView.setOnClickListener {

            val intent = Intent(context, TempsActivityItemsNextJourDescription::class.java)
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

