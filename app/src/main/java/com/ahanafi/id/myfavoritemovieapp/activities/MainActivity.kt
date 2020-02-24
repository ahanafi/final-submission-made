package com.ahanafi.id.myfavoritemovieapp.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.SectionsPagerAdapter
import com.ahanafi.id.myfavoritemovieapp.fragments.movie.MovieFragment
import com.ahanafi.id.myfavoritemovieapp.fragments.tvshow.TvShowFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var currentSelectedTab : String
    private lateinit var sectionPager : SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        sectionPager =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)

        //Fragment
        val movieFragment: Fragment = MovieFragment()
        val tvShowFragment: Fragment = TvShowFragment()

        sectionPager.addFragment(movieFragment)
        sectionPager.addFragment(tvShowFragment)

        viewPager.adapter = sectionPager
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setIconifiedByDefault(false)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.type_keyword)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val currentTab = tabs.selectedTabPosition
                currentSelectedTab = this@MainActivity.sectionPager.getPageTitle(currentTab).toString()
                val resultSearchIntent = Intent(this@MainActivity, ResultSearchActivity::class.java)
                resultSearchIntent.putExtra(ResultSearchActivity.EXTRA_KEYWORD, query)
                resultSearchIntent.putExtra(ResultSearchActivity.EXTRA_TYPE, currentSelectedTab)
                startActivity(resultSearchIntent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_change_language -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.action_favorite -> {
                val favoriteIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}