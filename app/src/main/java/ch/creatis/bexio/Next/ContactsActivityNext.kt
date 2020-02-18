package ch.creatis.bexio.Next

import android.content.Intent
import android.net.Uri
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



        var name_un = intent.getStringExtra("name_un")
        var name_deux = intent.getStringExtra("name_deux")
        var address = intent.getStringExtra("address")
        var postcode = intent.getStringExtra("postcode")
        var city = intent.getStringExtra("city")
        var country_id = intent.getStringExtra("country_id")
        var mail = intent.getStringExtra("mail")
        var mail_second = intent.getStringExtra("mail_second")
        var phone_fixed = intent.getStringExtra("phone_fixed")
        var phone_fixed_second = intent.getStringExtra("phone_fixed_second")
        var phone_mobile = intent.getStringExtra("phone_mobile")
        var fax = intent.getStringExtra("fax")
        var url = intent.getStringExtra("url")
        var skype_name = intent.getStringExtra("skype_name")



            // ------------------------------------------------------------------------------------------------------------------------------------

                if (name_un == "" || name_un == "null"){ nomLabel.visibility = View.GONE }else {nomLabel.text = intent.getStringExtra("name_un")}

            // ------------------------------------------------------------------------------------------------------------------------------------


                mailButton.setOnClickListener {
                    if (mail == "") {
                        Toast.makeText(this, "Il n'y pas de mail !", Toast.LENGTH_LONG).show()
                    } else {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.type = "plain/text"
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                        startActivity(Intent.createChooser(emailIntent, "Choisissez une application Mail"))
                    }

            }

            // ------------------------------------------------------------------------------------------------------------------------------------


                phoneButton.setOnClickListener {
                    if (phone_fixed == "") {
                        Toast.makeText(this, "Il n'y pas de téléphone !", Toast.LENGTH_LONG).show()
                    } else{
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${phone_fixed}")
                        startActivity(intent)
                    }

                }


            // ------------------------------------------------------------------------------------------------------------------------------------

            if (url == "" || url == "null"){
                siteWeb.visibility = View.GONE
                siteWebLabel.visibility = View.GONE
            } else{
                siteWebLabel.text = url
                siteWebLabel.setOnClickListener {



                    if (url.startsWith("http://") || url.startsWith("https://")){
                        var browser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browser)
                    }else{
                        var urlWith = "http://" + url
                        var browser = Intent(Intent.ACTION_VIEW, Uri.parse(urlWith))
                        startActivity(browser)

                    }



                }
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (address == "" || address == "null"){
                adresse.visibility = View.GONE
                adresseLabel.visibility = View.GONE
            } else{
                adresseLabel.text = address
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (postcode == "" || postcode == "null"){postcode = ""}
            if (city == "" || city == "null"){city = ""}
            if (country_id == "" || country_id == "null"){country_id = ""}


            var villeVariable = postcode + "" + city + "" + country_id.toString()


            if (villeVariable == ""){
                ville.visibility = View.GONE
                villeLabel.visibility = View.GONE
            } else{
                villeLabel.text = postcode + " " + city + " " + country_id.toString()
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (phone_mobile == "" || phone_mobile == "null"){
                mobile.visibility = View.GONE
                mobileLabel.visibility = View.GONE
            } else{
                mobileLabel.text = phone_mobile
                mobileLabel.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${phone_mobile}")
                    startActivity(intent)
                }
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (phone_fixed_second == "" || phone_fixed_second == "null"){
                telDeux.visibility = View.GONE
                telDeuxLabel.visibility = View.GONE
            } else{
                telDeuxLabel.text = phone_fixed_second
                telDeuxLabel.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${phone_fixed_second}")
                    startActivity(intent)
                }
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (mail_second == "" || mail_second == "null"){
                mailDeux.visibility = View.GONE
                mailDeuxLabel.visibility = View.GONE
            } else{
                mailDeuxLabel.text = mail_second
                mailDeuxLabel.setOnClickListener {
                    val emailIntent = Intent(Intent.ACTION_SEND)
                    emailIntent.type = "plain/text"
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail_second))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(emailIntent, "Choisissez une application Mail"))
                }
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (fax == "" || fax == "null"){
                faxTitre.visibility = View.GONE
                faxLabel.visibility = View.GONE
            } else{
                faxLabel.text = fax
            }

            // ------------------------------------------------------------------------------------------------------------------------------------

            if (skype_name == "" || skype_name == "null"){
                skype.visibility = View.GONE
                skypeLabel.visibility = View.GONE
            } else{
                skypeLabel.text = skype_name
            }



    }



}