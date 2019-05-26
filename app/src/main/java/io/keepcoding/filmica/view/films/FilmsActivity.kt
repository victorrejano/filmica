package io.keepcoding.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import io.keepcoding.filmica.R
import io.keepcoding.filmica.data.Film
import io.keepcoding.filmica.view.detail.DetailActivity
import io.keepcoding.filmica.view.detail.DetailFragment
import io.keepcoding.filmica.view.trends.TrendFilmsFragment
import io.keepcoding.filmica.view.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_DISCOVER = "discover"
const val TAG_WATCHLIST = "watchlist"
const val TAG_TREND = "trends"

class FilmsActivity : AppCompatActivity(),
    FilmsFragment.OnFilmClickLister {

    private lateinit var discoverFragment: DiscoverFilmsFragment
    private lateinit var watchlistFragment: WatchlistFragment
    private lateinit var trendFilmsFragment: TrendFilmsFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            setupFragments()
        } else {
            val tag = savedInstanceState.getString("active", TAG_DISCOVER)
            restoreFragments(tag)
        }

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_discover -> showMainFragment(discoverFragment)
                R.id.action_watchlist -> showMainFragment(watchlistFragment)
                R.id.action_trend -> showMainFragment(trendFilmsFragment)
            }

            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("active", activeFragment.tag)
    }

    private fun setupFragments() {
        discoverFragment = DiscoverFilmsFragment.newInstance()
        watchlistFragment = WatchlistFragment.newInstance()
        trendFilmsFragment = TrendFilmsFragment.newInstance()
        activeFragment = discoverFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.container, discoverFragment, TAG_DISCOVER)
            .add(R.id.container, watchlistFragment, TAG_WATCHLIST)
            .add(R.id.container, trendFilmsFragment, TAG_TREND)
            .hide(watchlistFragment)
            .hide(trendFilmsFragment)
            .commit()
    }

    private fun restoreFragments(tag: String) {
        discoverFragment = supportFragmentManager.findFragmentByTag(TAG_DISCOVER) as DiscoverFilmsFragment
        watchlistFragment =
            supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchlistFragment
        trendFilmsFragment = supportFragmentManager.findFragmentByTag(TAG_TREND) as TrendFilmsFragment

        when (tag) {
            TAG_WATCHLIST -> activeFragment = watchlistFragment
            TAG_DISCOVER -> activeFragment = discoverFragment
            TAG_TREND -> activeFragment = trendFilmsFragment
        }

    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment
    }

    override fun onClick(film: Film) {
        if (!isDetailDetailViewAvailable()) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", film.id)
            startActivity(intent)
        } else {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_detail,
                    DetailFragment.newInstance(film.id)
                )
                .commit()
        }
    }

    private fun isDetailDetailViewAvailable() =
        findViewById<FrameLayout>(R.id.container_detail) != null
}