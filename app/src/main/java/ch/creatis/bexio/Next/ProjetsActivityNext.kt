package ch.creatis.bexio.Next



import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import ch.creatis.bexio.Next.All.ProjetsActivityNextSaisieTaches
import ch.creatis.bexio.Next.All.ProjetsActivityNextSaisieTemps
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
        var pr_state_id = intent.getStringExtra("pr_state_id")
        var nr = intent.getStringExtra("nr")



        nomProjet.text = name
        if (end_date!= ""){
            date.text = start_date + "  -  " + end_date
        } else {
            date.text = start_date
        }
        description.text = comment



        if (pr_state_id == "1"){
            statutView.text = "Actif"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statutView.background = ContextCompat.getDrawable(this, R.drawable.projets_items_background_actif)
            } else {
                statutView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.projets_items_background_actif))
            }
        } else if(pr_state_id == "2"){
            statutView.text = "Ouvert"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statutView.background = ContextCompat.getDrawable(this, R.drawable.projets_items_background_ouvert)
            } else {
                statutView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.projets_items_background_ouvert))
            }
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