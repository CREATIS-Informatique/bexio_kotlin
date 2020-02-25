package ch.creatis.bexio.Next



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ch.creatis.bexio.R
import ch.creatis.bexio.Room.AppDatabase
import ch.creatis.bexio.Room.Semaines
import ch.creatis.bexio.Room.Temps
import ch.creatis.bexio.TempsAdapter
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
        println(tempsList.size)


    }



}


