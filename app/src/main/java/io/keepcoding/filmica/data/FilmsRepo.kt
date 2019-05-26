package io.keepcoding.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object FilmsRepo {

    private val films: HashSet<Film> = hashSetOf()

    @Volatile
    private var db: FilmDatabase? = null

    private fun getDbInstance(context: Context): FilmDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,
                FilmDatabase::class.java,
                "filmica-db"
            ).build()
        }

        return db as FilmDatabase
    }

    fun findFilmById(id: String): Film? {
        return films.find {
            return@find it.id == id
        }

    }

    fun saveFilm(
        context: Context,
        film: Film,
        callback: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }

            async.await()
            callback.invoke(film)
        }
    }

    fun getFilms(
        context: Context,
        callback: (List<Film>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {

            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }

            val films = async.await()
            callback.invoke(films)
        }
    }

    fun deleteFilm(
        context: Context,
        film: Film,
        callback: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }

            async.await()
            callback.invoke(film)
        }
    }

    fun discoverFilms(
        context: Context,
        page: Int = 1,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = ApiRoutes.discoverMoviesUrl(page)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val films =
                    Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.films.addAll(films)
                onResponse.invoke(FilmsRepo.films.toList())
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)
    }

    fun trendFilms(
        context: Context,
        page: Int = 1,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {

        val url = ApiRoutes.trendMoviesUrl(page)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val films =
                    Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.films.addAll(films)
                onResponse.invoke(FilmsRepo.films.toList())
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)

    }

    fun searchFilms(
        context: Context,
        query: String,
        onResponse: (List<Film>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {

        val url = ApiRoutes.searchMovies(query)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val films =
                    Film.parseFilms(response.getJSONArray("results"))
                FilmsRepo.films.addAll(films)
                val results = if (films.count() > 10) films.subList(0, 10) else films
                onResponse.invoke(results.toList())
            },
            { error ->
                error.printStackTrace()
                onError.invoke(error)
            }
        )

        Volley.newRequestQueue(context)
            .add(request)

    }

    private fun dummyFilms(): MutableList<Film> {
        return (1..10).map { i: Int ->
            return@map Film(
                id = "${i}",
                title = "Film ${i}",
                overview = "Overview ${i}",
                genre = "Genre ${i}",
                rating = i.toFloat(),
                date = "2019-05-${i}"
            )
        }.toMutableList()
    }
}