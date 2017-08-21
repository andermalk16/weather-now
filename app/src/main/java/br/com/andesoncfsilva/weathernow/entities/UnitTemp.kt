package br.com.andesoncfsilva.weathernow.entities

/**
 * Created by Anderson Silva on 19/08/17.
 *
 */
enum class UnitTemp(val value: String) {
    CELSIUS("C"),
    FAHRENHEIT("F");

    override fun toString(): String {
        return if (this == UnitTemp.CELSIUS)
            "metric"
        else if (this == UnitTemp.FAHRENHEIT)
            "imperial"
        else
            "no_value"
    }
}