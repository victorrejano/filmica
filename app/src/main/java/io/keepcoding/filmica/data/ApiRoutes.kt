package io.keepcoding.filmica.data

import android.net.Uri
import io.keepcoding.filmica.BuildConfig

object ApiRoutes {

    fun discoverMoviesUrl(
        language: String = "en-US",
        sort: String = "popularity.desc"
    ): String {
        return getUriBuilder()
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("language", language)
            .appendQueryParameter("sort_by", sort)
            .build()
            .toString()
    }

    fun trendMoviesUrl(
        language: String = "en-US",
        timeWindow: String = "week"
    ): String {

        return getUriBuilder()
            .appendPath("trending")
            .appendPath("movie")
            .appendPath(timeWindow)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun searchMovies(
        query: String,
        language: String = "en-US"
    ): String {

        return getUriBuilder().appendPath("search")
            .appendPath("movie")
            .appendQueryParameter("language", language)
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }

    private fun getUriBuilder(): Uri.Builder =
        Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendQueryParameter("api_key", BuildConfig.MovieDbApiKey)
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
}