package ch.creatis.bexio.Next.Tab


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.creatis.bexio.R



class ActivityProjetsNextSaisieTempsFirst : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_activity_projets_next_saisie_temps_first, container, false)
    }

}
