package br.com.andesoncfsilva.weathernow.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(@SerializedName("cnt")
                                  @Expose
                                  var cnt: Int? = null,
                                  @SerializedName("list")
                                  @Expose
                                  var city: List<City>? = null)