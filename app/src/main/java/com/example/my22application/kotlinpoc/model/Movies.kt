package com.example.my22application.kotlinpoc.model

class Movies {
    companion object Factory {
        fun create(): Movies = Movies()
    }
    var movieId: String? = null
    var movieName: String? = null
    var movieGenre: String? = null
    var movieImage: String? = null
    var movieYear: String? = null
    var movieDirector: String? = null
}