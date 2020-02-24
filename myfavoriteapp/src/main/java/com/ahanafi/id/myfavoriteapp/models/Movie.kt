package com.ahanafi.id.myfavoriteapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie  (
    var title : String? = null,
    var voteCount : Int? = 0,
    var backdropPath : String? = null,
    var posterPath : String? = null,
    var overview : String? = null,
    var releaseDate : String? = null,
    var language : String? = null,
    var voteAverage : Number? = 0,
    var popularity : Number? = 0,
    var id : Int? = 0
) : Parcelable