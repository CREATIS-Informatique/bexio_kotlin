package ch.creatis.bexio.Reglages



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_reglages.*


class ReglagesActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reglages)



        enregistrerButton.setOnClickListener {

            println(clientIdText.text.toString())
            println(clientSecretText.text.toString())

        }



    }



}
