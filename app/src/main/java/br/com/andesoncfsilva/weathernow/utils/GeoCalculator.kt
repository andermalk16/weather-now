package br.com.andesoncfsilva.weathernow.utils

import android.location.Location
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.GeoBox

/**
 * Created by Anderson Silva on 21/08/17.
 */
class GeoCalculatorImpl : GeoCalculator {


    override fun calculateBox(latitude: Double, longitude: Double): GeoBox {
        val radiusMetersSearch = 100000
        val earthSize = 6378000
        //calculate a bounding box around the geo point
        // 6378000 Size of the Earth (in meters)
        val longitudeD = Math.asin(radiusMetersSearch / (earthSize * Math.cos(Math.PI * latitude / 180))) * 180 / Math.PI
        val latitudeD = Math.asin(radiusMetersSearch.toDouble() / earthSize.toDouble()) * 180 / Math.PI

        return GeoBox(
                latitudeMax = latitude + latitudeD,
                latitudeMin = latitude - latitudeD,
                longitudeMax = longitude + longitudeD,
                longitudeMin = longitude - longitudeD)

    }

    override fun calculateDistance(city: CityWeather, latitude: Double, longitude: Double): Double {

        val locationA = Location("A")
        locationA.latitude = latitude
        locationA.longitude = longitude
        val locationB = Location("B")
        locationB.latitude = city.latitude
        locationB.longitude = city.longitude
        return locationA.distanceTo(locationB).toDouble()

    }
}

interface GeoCalculator {

    fun calculateBox(latitude: Double, longitude: Double): GeoBox
    fun calculateDistance(city: CityWeather, latitude: Double, longitude: Double): Double
}