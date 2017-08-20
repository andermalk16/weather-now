package br.com.andesoncfsilva.weathernow.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse {

    @SerializedName("cod")
    @Expose
    var cod: String? = null
    @SerializedName("calctime")
    @Expose
    var calctime: Double? = null
    @SerializedName("cnt")
    @Expose
    var cnt: Int? = null
    @SerializedName("list")
    @Expose
    var city: List<City>? = null

}
