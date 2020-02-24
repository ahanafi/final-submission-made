package com.ahanafi.id.myfavoriteapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ahanafi.id.myfavoriteapp.R
import com.ahanafi.id.myfavoriteapp.adapters.FavoritePagerAdapter
import com.ahanafi.id.myfavoriteapp.fragments.MovieFavoriteFragment
import com.ahanafi.id.myfavoriteapp.fragments.TvShowFavoriteFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val favoritePagerAdapter = FavoritePagerAdapter(this, supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.favorite_view_pager)
        val favoriteTabs = findViewById<TabLayout>(R.id.favorite_tabs)

        val movieFragment : Fragment =
            MovieFavoriteFragment()
        val tvShowFragment : Fragment =
            TvShowFavoriteFragment()

        favoritePagerAdapter.addFavoriteFragment(movieFragment)
        favoritePagerAdapter.addFavoriteFragment(tvShowFragment)

        viewPager.adapter = favoritePagerAdapter
        favoriteTabs.setupWithViewPager(viewPager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
