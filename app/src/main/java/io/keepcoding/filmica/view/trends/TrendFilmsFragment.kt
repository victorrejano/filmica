package io.keepcoding.filmica.view.trends

import io.keepcoding.filmica.data.FilmsRepo
import io.keepcoding.filmica.view.films.FilmsFragment

class TrendFilmsFragment : FilmsFragment() {

    companion object {
        fun newInstance() = TrendFilmsFragment()
    }

    override fun reload(page: Int) {
        showProgress()

        FilmsRepo.trendFilms(context!!, page,
            { films ->
                // if include paginate
                adapter.addFilms(films)
                showList()
            }, { errorRequest ->
                showError()
            })
    }
}