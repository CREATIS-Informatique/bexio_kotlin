package ch.creatis.bexio.Next

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_contacts_next.*

class ContactsActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_next)


            nomLabel.text = intent.getStringExtra("name_un")
    }


}
