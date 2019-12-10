package ch.creatis.bexio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        loginButton.setOnClickListener {

            webView.settings.javaScriptEnabled = true // enable javascript

            webView.webViewClient = object : WebViewClient() {


                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)


                    if (url!!.contains("code")) {

                        val chars = url
                        val codeun = chars!!.dropLast(18)

                        CODEFINAL = codeun.drop(16)



                    }



                }



            }


            webView.loadUrl("https://office.bexio.com/oauth/authorize?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&state=creatis2019&scope=task_show%20project_show%20monitoring_edit")


        }



    }

    companion object {

        var CODEFINAL = ""

    }

}
