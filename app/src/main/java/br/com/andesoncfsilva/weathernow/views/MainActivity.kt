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
import br.com.andesoncfsilva.weathernow.views.base.MvpActivity
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
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
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
        setupFragment()
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

    override fun onResume() {
        super.onResume()
        if (checkPlayServicesAvailable()) {
            presenter?.setDefaultCameraPosition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.menu_temp_c)?.isVisible = currentUnit == UnitTemp.FAHRENHEIT
        menu?.findItem(R.id.menu_temp_f)?.isVisible = currentUnit == UnitTemp.CELSIUS
        menu?.findItem(R.id.menu_list)?.isVisible = currentLayout == WEATHER_MODE_MAP
        menu?.findItem(R.id.menu_map)?.isVisible = currentLayout == WEATHER_MODE_LIST
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_temp_c -> {
                currentUnit = UnitTemp.CELSIUS
                presenter?.getWeatherList(currentUnit, this.currentLatitude, this.currentLongitude)

            }
            R.id.menu_temp_f -> {
                currentUnit = UnitTemp.FAHRENHEIT
                presenter?.getWeatherList(currentUnit, this.currentLatitude, this.currentLongitude)

            }
            R.id.menu_list -> {
                currentLayout = WEATHER_MODE_LIST

            }
            R.id.menu_map -> {
                currentLayout = WEATHER_MODE_MAP
            }
        }
        setupFragment()
        invalidateOptionsMenu()

        return super.onOptionsItemSelected(item)
    }

    private fun setupFragment() {
        listFragmentContainer.visibility = if (currentLayout == WEATHER_MODE_LIST) View.VISIBLE else View.GONE
        mapFragmentContainer.visibility = if (currentLayout == WEATHER_MODE_MAP) View.VISIBLE else View.GONE
    }


    override fun setCameraPosition(latitude: Double, longitude: Double) {
        EventBus.getDefault().post(SetCameraPosition(latitude, longitude))
        this.currentLatitude = latitude
        this.currentLongitude = longitude
        presenter?.getWeatherList(currentUnit, this.currentLatitude, this.currentLongitude)
    }

    override fun showCitiesWeather(cities: List<CityWeather>) {
        EventBus.getDefault().post(ShowCitiesWeather(cities.toList()))
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.INVISIBLE
    }

    override fun showError(e: Throwable) {
        Snackbar.make(findViewById(android.R.id.content), errorMessageFactory.create(e), Snackbar.LENGTH_INDEFINITE).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCities(e: UpdateCities) {
        currentLatitude = e.latitude
        currentLongitude = e.longitude
        presenter?.getWeatherList(currentUnit, e.latitude, e.longitude)
    }

    private fun checkPlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val status = apiAvailability.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(this, status, 1).show()
            } else {

            }
            return false
        }

        return true
    }
}
