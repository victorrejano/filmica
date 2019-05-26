package io.keepcoding.filmica.view.trends

import io.keepcoding.filmica.data.FilmsRepo
import io.keepcoding.filmica.view.films.FilmsFragment

class TrendFilmsFragment: FilmsFragment() {

    companion object {
        fun newInstance() = TrendFilmsFragment()
    }

    override fun reload() {
        showProgress()

        FilmsRepo.trendFilms(context!!,
            { films ->
                adapter.setFilms(films)
                showList()

            }, { errorRequest ->
                showError()
            })
    }
}