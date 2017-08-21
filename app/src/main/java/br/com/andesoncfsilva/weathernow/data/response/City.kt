package br.com.andesoncfsilva.weathernow.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class City (

    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null,
    @SerializedName("main")
    @Expose
    var main: Main? = null,
    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null)
