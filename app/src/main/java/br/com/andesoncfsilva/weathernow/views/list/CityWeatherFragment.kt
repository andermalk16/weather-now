package br.com.andesoncfsilva.weathernow.views.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.views.ShowCitiesWeather
import kotlinx.android.synthetic.main.fragment_cityweather_list.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class CityWeatherFragment : Fragment() {

    private var adapter: MyCityWeatherRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_cityweather_list, container, false)
        retainInstance = true

        view.list.layoutManager = LinearLayoutManager(context)
        adapter = MyCityWeatherRecyclerViewAdapter(context)
        view.list.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCities(e: ShowCitiesWeather) {
        adapter?.list = e.list
        adapter?.notifyDataSetChanged()
    }
}
