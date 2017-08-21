package br.com.andesoncfsilva.weathernow.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather (

    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("icon")
    @Expose
    var icon: String? = null
)
