package ch.creatis.bexio.TachesTemps.Temps.FirstSecond.Pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ch.creatis.bexio.TachesTemps.Temps.FirstSecond.ActivityProjetsNextSaisieTempsFirst
import ch.creatis.bexio.TachesTemps.Temps.FirstSecond.ActivityProjetsNextSaisieTempsSecond

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ActivityProjetsNextSaisieTempsFirst()
            }
            else -> {
                return ActivityProjetsNextSaisieTempsSecond()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Un jour"
            else -> {
                return "Plusieurs jours"
            }
        }
    }

}