package ch.creatis.bexio



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject



class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")
        if (refreshToken == ""){ webViewIsVisible() } else {getAccessTokenAllTime()}



    }



    fun webViewIsVisible(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()



        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {



            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)



                if (url!!.contains("code")) {



                    val chars = url
                    val codeun = chars!!.dropLast(18)



                    // -------------------------------------------------------------------------------------

                    editor.putString("CODETOKEN", codeun.drop(16))
                    editor.commit()
                    getAccessTokenFirstTime()
//                    webView.visibility = View.INVISIBLE

                    // -------------------------------------------------------------------------------------



                }
            }
        }



        webView.loadUrl("https://office.bexio.com/oauth/authorize?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&state=creatis2019&scope=contact_show project_show task_show monitoring_edit")



    }



    fun getAccessTokenFirstTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var codeToken = sharedPreferences.getString("CODETOKEN", "")



        val queue = Volley.newRequestQueue(this)

        val url = "https://office.bexio.com/oauth/access_token?client_id=4707003559.apps.bexio.com&redirect_uri=bxpocket://auth&client_secret=nRA79oKLwIIRh7NOH5TNavWabEE=&$codeToken"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->

            var responseJsonObj = JSONObject(response)


            // -------------------------------------------------------------------------------------

            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("ORG", responseJsonObj.getString("org"))
            editor.commit()



            val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentAct)

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }



    fun getAccessTokenAllTime(){

        val sharedPreferences = getSharedPreferences("Bexio", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var refreshToken = sharedPreferences.getString("REFRESHTOKEN", "")



        val queue = Volley.newRequestQueue(this)

        val url = "https://office.bexio.com/oauth/refresh_token?client_id=4707003559.apps.bexio.com&client_secret=nRA79oKLwIIRh7NOH5TNavWabEE=&refresh_token=$refreshToken"

        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->



            // -------------------------------------------------------------------------------------

            var responseJsonObj = JSONObject(response)
            editor.putString("ACCESSTOKEN", responseJsonObj.getString("access_token"))
            editor.putString("REFRESHTOKEN", responseJsonObj.getString("refresh_token"))
            editor.putString("ORG", responseJsonObj.getString("org"))
            editor.commit()



            val intentAct = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentAct)

            // -------------------------------------------------------------------------------------



        }, Response.ErrorListener {})

        queue.add(stringRequest)



    }



}