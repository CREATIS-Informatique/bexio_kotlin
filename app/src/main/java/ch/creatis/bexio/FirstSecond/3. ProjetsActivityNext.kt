package ch.creatis.bexio.FirstSecond



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.TachesTemps.Taches.ProjetsActivityNextSaisieTaches
import ch.creatis.bexio.TachesTemps.Temps.First.ProjetsActivityNextSaisieTemps
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_projets_next.*



class ProjetsActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next)



        var name = intent.getStringExtra("name")
        var start_date = intent.getStringExtra("start_date")
        var end_date = intent.getStringExtra("end_date")
        var comment = intent.getStringExtra("comment")
        var pr_state_id = intent.getStringExtra("pr_state_id").toInt()
        var nr = intent.getStringExtra("nr")



        nomProjet.text = name
        if (end_date!= ""){
            date.text = start_date + "  -  " + end_date
        } else {
            date.text = start_date
        }
        description.text = comment



         // Statut
        if(pr_state_id == 1){
            statutView.text = "En suspens"
            statutView.setBackgroundResource(R.drawable.projets_activity_items_status_en_suspens)
        } else if (pr_state_id == 2){
            statutView.text = "Actif"
            statutView.setBackgroundResource(R.drawable.projets_activity_items_status_actif)
        } else if (pr_state_id == 3){
            statutView.text = "Archivé"
           statutView.setBackgroundResource(R.drawable.projets_activity_items_status_archive)
        }



        saisirTempsProjet.setOnClickListener {

            val intent = Intent(this, ProjetsActivityNextSaisieTemps::class.java)
            startActivity(intent)

        }



        saisirTache.setOnClickListener {

            val intent = Intent(this, ProjetsActivityNextSaisieTaches::class.java)
            startActivity(intent)

        }



    }



}