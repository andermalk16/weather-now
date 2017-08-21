package br.com.andesoncfsilva.weathernow.views.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.views.HideLoading
import br.com.andesoncfsilva.weathernow.views.RefreshWeatherCities
import br.com.andesoncfsilva.weathernow.views.ShowCitiesWeather
import br.com.andesoncfsilva.weathernow.views.ShowLoading
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

    private var list: RecyclerView? = null

    private var srList: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_cityweather_list, container, false)
        retainInstance = true

        list = view.list
        view.list.layoutManager = LinearLayoutManager(context)
        adapter = MyCityWeatherRecyclerViewAdapter(context)
        view.list.adapter = adapter
        srList = view.srList
        view.srList.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryLight)
        view.srList.setOnRefreshListener { EventBus.getDefault().post(RefreshWeatherCities()) }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showLoading(e: ShowLoading) {
        srList?.isRefreshing = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun hideLoading(e: HideLoading) {
        srList?.isRefreshing = false
    }
}
