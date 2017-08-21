package br.com.andesoncfsilva.weathernow.views

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.entities.UnitTemp
import br.com.andesoncfsilva.weathernow.exception.factory.ErrorMessageFactory
import br.com.andesoncfsilva.weathernow.utils.safeLet
import br.com.andesoncfsilva.weathernow.views.base.MvpActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class MainActivity : MvpActivity<MainView, MainPresenter>(),
        MainView {

    private val WEATHER_MODE_LIST = "WEATHER_MODE_LIST"
    private val WEATHER_MODE_MAP = "WEATHER_MODE_MAP"
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private var currentUnit = UnitTemp.CELSIUS
    private var currentLayout = WEATHER_MODE_LIST

    @Inject lateinit var mainPresenter: MainPresenter
    @Inject lateinit var errorMessageFactory: ErrorMessageFactory

    override fun createPresenter(): MainPresenter = mainPresenter

    override fun initializeInjector() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        if (checkPlayServicesAvailable()) {
            presenter?.setDefaultCameraPosition()
        }
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.menu_temp_c)?.isVisible = currentUnit == UnitTemp.FAHRENHEIT
        menu?.findItem(R.id.menu_temp_f)?.isVisible = currentUnit == UnitTemp.CELSIUS
        menu?.findItem(R.id.menu_list)?.isVisible = currentLayout == WEATHER_MODE_MAP
        menu?.findItem(R.id.menu_map)?.isVisible = currentLayout == WEATHER_MODE_LIST
        return true
    }

    override fun hideFragments() {
        listFragmentContainer.visibility = View.GONE
        mapFragmentContainer.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_temp_c -> {
                currentUnit = UnitTemp.CELSIUS
                safeLet(this.currentLatitude, this.currentLongitude) { lat, lon ->
                    presenter?.getWeatherList(currentUnit, lat, lon)
                }

            }
            R.id.menu_temp_f -> {
                currentUnit = UnitTemp.FAHRENHEIT
                safeLet(this.currentLatitude, this.currentLongitude) { lat, lon ->
                    presenter?.getWeatherList(currentUnit, lat, lon)
                }
            }
            R.id.menu_list -> {
                currentLayout = WEATHER_MODE_LIST
                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(flContainerFrags)
                showFragments()
                refreshMenu()
            }
            R.id.menu_map -> {
                currentLayout = WEATHER_MODE_MAP
                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(flContainerFrags)
                showFragments()
                refreshMenu()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun refreshMenu(){
        invalidateOptionsMenu()
    }

    override fun showFragments() {
        listFragmentContainer.visibility = if (currentLayout == WEATHER_MODE_LIST) View.VISIBLE else View.GONE
        mapFragmentContainer.visibility = if (currentLayout == WEATHER_MODE_MAP) View.VISIBLE else View.GONE
    }


    override fun setCameraPosition(latitude: Double, longitude: Double) {
        EventBus.getDefault().post(SetCameraPosition(latitude, longitude))
        this.currentLatitude = latitude
        this.currentLongitude = longitude
        safeLet(this.currentLatitude, this.currentLongitude) { lat, lon ->
            presenter?.getWeatherList(currentUnit, lat, lon)
        }
    }

    override fun showCitiesWeather(cities: List<CityWeather>) {
        EventBus.getDefault().post(ShowCitiesWeather(cities.toList()))
    }

    override fun showLoading() {
        EventBus.getDefault().post(ShowLoading())
    }

    override fun hideLoading() {
        EventBus.getDefault().post(HideLoading())
    }

    override fun showError(e: Throwable?) {
        e?.let {
            Snackbar.make(findViewById(android.R.id.content), errorMessageFactory.create(e), Snackbar.LENGTH_LONG).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCities(e: SearchNewWeatherCities) {
        currentLatitude = e.latitude
        currentLongitude = e.longitude
        presenter?.getWeatherList(currentUnit, e.latitude, e.longitude)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshWeatherCities(e: RefreshWeatherCities) {
        safeLet(this.currentLatitude, this.currentLongitude) { lat, lon ->
            presenter?.getWeatherList(currentUnit, lat, lon)
        }
    }

    private fun checkPlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val status = apiAvailability.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(this, status, 1).show()
            }
            return false
        }

        return true
    }
}
