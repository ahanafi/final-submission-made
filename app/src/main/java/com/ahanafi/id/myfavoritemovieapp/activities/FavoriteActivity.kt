package com.ahanafi.id.myfavoritemovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.FavoritePagerAdapter
import com.ahanafi.id.myfavoritemovieapp.fragments.movie.MovieFavoriteFragment
import com.ahanafi.id.myfavoritemovieapp.fragments.tvshow.TvShowFavoriteFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val favoritePagerAdapter = FavoritePagerAdapter(this, supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.favorite_view_pager)
        val favoriteTabs = findViewById<TabLayout>(R.id.favorite_tabs)

        val movieFragment : Fragment = MovieFavoriteFragment()
        val tvShowFragment : Fragment = TvShowFavoriteFragment()

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
