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



        var user_id = intent.getStringExtra("user_id")
        var finish_date = intent.getStringExtra("finish_date")
        var subject = intent.getStringExtra("subject")
        var contact_id = intent.getIntExtra("contact_id",0)



        projectName.text = "Test"
        tacheName.text = subject
        dureeName.text = finish_date
        interlocuteurName.text = user_id
        contactName.text = contact_id.toString()



        saisirTempsTache.setOnClickListener {

            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)

        }



    }



}