package ch.creatis.bexio.Next

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.activity_contacts_next.*

class ContactsActivityNext : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_next)



            val name_un = intent.getStringExtra("name_un")
            val name_deux = intent.getStringExtra("name_deux")
            val address = intent.getStringExtra("address")
            val postcode = intent.getStringExtra("postcode")
            val city = intent.getStringExtra("city")
            val country_id = intent.getStringExtra("country_id")
            val mail = intent.getStringExtra("mail")
            val mail_second = intent.getStringExtra("mail_second")
            val phone_fixed = intent.getStringExtra("phone_fixed")
            val phone_fixed_second = intent.getStringExtra("phone_fixed_second")
            val phone_mobile = intent.getStringExtra("phone_mobile")
            val fax = intent.getStringExtra("fax")
            val url = intent.getStringExtra("url")
            val skype_name = intent.getStringExtra("skype_name")



            if (name_un == "" || name_un == "null"){ nomLabel.visibility = View.GONE }else {nomLabel.text = intent.getStringExtra("name_un")}
            // ------------------------------------------------------------------------------------------------------------------------------------
            if (url == "" || url == "null"){
                siteWeb.visibility = View.GONE
                siteWebLabel.visibility = View.GONE
            } else{
                siteWebLabel.text = url
            }
            // ------------------------------------------------------------------------------------------------------------------------------------



    }



}
