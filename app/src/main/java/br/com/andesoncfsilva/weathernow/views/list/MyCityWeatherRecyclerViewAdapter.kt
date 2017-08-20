package br.com.andesoncfsilva.weathernow.views.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cityweather.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyCityWeatherRecyclerViewAdapter(val context: Context) : RecyclerView.Adapter<MyCityWeatherRecyclerViewAdapter.ViewHolder>() {

    var list: List<CityWeather> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cityweather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(cityWeather: CityWeather) {
            mView.tvCityName.text = cityWeather.cityName
            Picasso.with(context).load(cityWeather.weatherImageUrl).into(mView.ivIconWeather)
            mView.tvWeatherDescription.text = cityWeather.weatherDescription.capitalize()
            mView.tvCityDistance.text = context.getString(R.string.distance_km, (cityWeather.userDistance / 1000))
            mView.tvCurrentTemp.text = context.getString(R.string.current_temp, cityWeather.currentTemperature.toInt().toString(), cityWeather.unit.value)
            mView.tvTempMin.text = context.getString(R.string.min_temp,cityWeather.minTemperature.toInt().toString(), cityWeather.unit.value)
            mView.tvTempMax.text = context.getString(R.string.max_temp,cityWeather.maxTemperature.toInt().toString(), cityWeather.unit.value)
        }
    }
}
