package io.keepcoding.filmica.view.films

import io.keepcoding.filmica.data.FilmsRepo

class DiscoverFilmsFragment : FilmsFragment() {

    companion object {
        fun newInstance() = DiscoverFilmsFragment()
    }

    override fun reload(page: Int) {
        showProgress()

        FilmsRepo.discoverFilms(context!!, page,
            { films ->
                // if include paginate
                adapter.addFilms(films)
                showList()
            }, { errorRequest ->
                showError()
            })
    }
}