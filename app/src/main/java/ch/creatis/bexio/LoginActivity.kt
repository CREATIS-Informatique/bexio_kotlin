package ch.creatis.bexio



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



            webView.settings.javaScriptEnabled = true // enable javascript
            webView.webViewClient = object : WebViewClient() {



                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)



                    if (url!!.contains("code")) {
                        val chars = url
                        val codeun = chars!!.dropLast(18)
                        CODETOKEN = codeun.drop(16)



                        webView.visibility = View.INVISIBLE
                        getAccessToken()


                    }
                }
            }



            webView.loadUrl("https://office.bexio.com/oauth/authorize?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&state=creatis2019&scope=task_show%20project_show%20monitoring_edit")



    }



    fun getAccessToken(){



        val queue = Volley.newRequestQueue(this)

        val url = "https://office.bexio.com/oauth/access_token?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&client_secret=nRA79oKLwIIRh7NOH5TNavWabEE=&$CODETOKEN"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->

            println(response)

        }, Response.ErrorListener {})

        queue.add(stringRequest)



//        val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
//        startActivity(intentAct)



    }



    companion object {

        var CODETOKEN = ""
        var ACCESSTOKEN = ""
        var REFRESHTOKEN = ""

    }



}