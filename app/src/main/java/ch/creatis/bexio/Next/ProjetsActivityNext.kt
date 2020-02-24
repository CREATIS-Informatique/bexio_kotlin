package ch.creatis.bexio.Next

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.R

class ProjetsActivityNext : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets_next)



        var name = intent.getStringExtra("name")
        var start_date = intent.getStringExtra("start_date")
        var end_date = intent.getStringExtra("end_date")
        var comment = intent.getStringExtra("comment")
        var nr = intent.getStringExtra("nr")
        var pr_state_id = intent.getStringExtra("pr_state_id")



    }



}
