package io.keepcoding.filmica.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@Entity
data class Film(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "poster_id") val posterId: String = "",
    val title: String = "No title",
    val genre: String = "No genre",
    val rating: Float = 0.0f,
    val overview: String = "No overview",
    val date: String = "1999-09-19"
) {
    @Ignore
    constructor() : this("")

    fun getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500/${posterId}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Film

        if (id != other.id) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }

    companion object {
        fun parseFilm(jsonFilm: JSONObject): Film {
            with(jsonFilm) {
                return Film(
                    id = getInt("id").toString(),
                    title = getString("title"),
                    overview = getString("overview"),
                    rating = getDouble("vote_average").toFloat(),
                    date = getString("release_date"),
                    posterId = optString("poster_path", ""),
                    genre = parseGenres(
                        jsonFilm.getJSONArray(
                            "genre_ids"
                        )
                    )
                )
            }
        }

        fun parseFilms(filmsArray: JSONArray): List<Film> {
            val films = mutableListOf<Film>()
            for (i in 0 until filmsArray.length()) {
                val film =
                    parseFilm(filmsArray.getJSONObject(i))
                films.add(film)
            }

            return films
        }

        private fun parseGenres(genresArray: JSONArray): String {

            if (genresArray.length() == 0) return ""

            val genres = mutableListOf<String>()

            for (i in 0 until genresArray.length()) {
                val genreId = genresArray.getInt(i)
                val genre = ApiConstants.genres[genreId] ?: ""
                genres.add(genre)
            }

            return genres.reduce { acc, genre -> "$acc | $genre" }

        }
    }


}