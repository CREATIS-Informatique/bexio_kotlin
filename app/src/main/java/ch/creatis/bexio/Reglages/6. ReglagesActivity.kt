package ch.creatis.bexio.Reglages



import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.x_activity_reglages.*


class ReglagesActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.x_activity_reglages)



        enregistrerButton.setOnClickListener {

            println(clientIdText.text.toString())
            println(clientSecretText.text.toString())

        }


        // Se déconnecter
        seDeconnecterButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Voulez-vous vraiment vous déconecter ?")
            builder.setMessage("L'application va surppimer toutes vos données")
            builder.setPositiveButton("Oui"){ _, _ ->

                if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                    (this?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                        .clearApplicationUserData() // note: it has a return value!
                } else {

                }

            }

            builder.setNeutralButton("Annuler"){_,_ ->

            }

            val dialog: AlertDialog = builder.create()
            dialog.show()


        }



    }



}
