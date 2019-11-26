package ch.creatis.bexio

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_projets.*
import kotlinx.android.synthetic.main.activity_projets_items.view.*

class ProjetsActivity : AppCompatActivity() {




    val tableau: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projets)



        tableau.add("Application")
        tableau.add("Application")
        tableau.add("Application")
        tableau.add("Application")
        tableau.add("Application")
        tableau.add("Application")
        tableau.add("Application")



        recyclerViewProjets.layoutManager = LinearLayoutManager(this)
        recyclerViewProjets.adapter = ProjetsAdapter(tableau, this)



    }



}



class ProjetsAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ProjetsHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjetsHolder {
        return ProjetsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.activity_projets_items,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ProjetsHolder, position: Int) {
        holder.viewTemps?.text = items.get(position)
    }



    override fun getItemCount(): Int {
        return items.size
    }



}



class ProjetsHolder (view: View) : RecyclerView.ViewHolder(view) {

    val viewTemps = view.projetLabel

}