package io.keepcoding.filmica.view.films

import io.keepcoding.filmica.data.FilmsRepo

class DiscoverFilmsFragment : FilmsFragment(){

    companion object {
        fun newInstance() = DiscoverFilmsFragment()
    }

    override fun reload() {
        showProgress()

        FilmsRepo.discoverFilms(context!!,
            { films ->
                adapter.setFilms(films)
                showList()

            }, { errorRequest ->
                showError()
            })
    }
}