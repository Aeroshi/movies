package com.aeroshi.movies.data

data class Movies(
    val status: String,
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: ArrayList<Result>
)


