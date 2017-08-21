package br.com.andesoncfsilva.weathernow.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coord(@SerializedName("Lon")
                 @Expose
                 var lon: Double? = null,
                 @SerializedName("Lat")
                 @Expose
                 var lat: Double? = null)
