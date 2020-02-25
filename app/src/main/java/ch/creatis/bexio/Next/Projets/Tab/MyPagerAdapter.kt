package ch.creatis.bexio.Next.Projets.Tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

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