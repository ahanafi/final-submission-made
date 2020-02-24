package com.ahanafi.id.myfavoriteapp.adapters

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ahanafi.id.myfavoriteapp.R

class FavoritePagerAdapter(private val context: Context, fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listFavoriteFragment = ArrayList<Fragment>()
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_movie, R.string.tab_tv_show)

    override fun getItem(position: Int): Fragment{
        return listFavoriteFragment[position]
    }

    override fun getCount(): Int {
        return listFavoriteFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    fun addFavoriteFragment(fragment: Fragment) {
        listFavoriteFragment.add(fragment)
    }

}