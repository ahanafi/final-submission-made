package com.ahanafi.id.myfavoritemovieapp.adapters

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ahanafi.id.myfavoritemovieapp.R
import java.util.*

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listFragment = ArrayList<Fragment>()
    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_movie, R.string.tab_tv_show
    )

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = listFragment.size

    fun addFragment(fragment: Fragment) {
        listFragment.add(fragment)
    }
}
