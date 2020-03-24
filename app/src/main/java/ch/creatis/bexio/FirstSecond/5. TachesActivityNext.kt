package ch.creatis.bexio.FirstSecond



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.TachesTemps.Temps.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_taches_next.*



class TachesActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taches_next)



        saisirTempsTache.setOnClickListener {

            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)

        }



    }



}
