package io.keepcoding.filmica.view.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import io.keepcoding.filmica.R
import io.keepcoding.filmica.data.Film
import io.keepcoding.filmica.data.FilmsRepo
import io.keepcoding.filmica.view.watchlist.FilmListAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    val adapter: FilmListAdapter = FilmListAdapter {
        showDetail(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchList.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 3) {
                    makeSearch(query)
                } else {
                    Toast.makeText(
                        context,
                        "Your search query must contain at least three characters",
                        Toast.LENGTH_LONG
                    ).show()
                }

                return true
            }
        })

        // If user cancels search, clear latest results
        searchView.setOnCloseListener {
            adapter.setFilms(listOf())
            true
        }
    }

    private fun makeSearch(query: String) {
        showProgress()
        FilmsRepo.searchFilms(context!!, query, {
            adapter.setFilms(it)

            if (it.isNotEmpty()) showList() else showEmptyView()

        }, {
            adapter.setFilms(listOf())
            showEmptyView()
        })
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

    private fun showDetail(film: Film) {
        Toast.makeText(context!!, "Detail showed", Toast.LENGTH_LONG).show()
    }

    private fun showList() {
        searchProgress.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE
        searchList.visibility = View.VISIBLE
    }

    private fun showEmptyView() {
        searchProgress.visibility = View.INVISIBLE
        searchList.visibility = View.INVISIBLE
        emptyView.visibility = View.VISIBLE
    }

    private fun showProgress() {
        searchProgress.visibility = View.VISIBLE
        emptyView.visibility = View.INVISIBLE
        searchList.visibility = View.INVISIBLE
    }
}
