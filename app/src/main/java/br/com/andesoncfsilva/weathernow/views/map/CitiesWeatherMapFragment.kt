package br.com.andesoncfsilva.weathernow.views.map

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.andesoncfsilva.weathernow.R
import br.com.andesoncfsilva.weathernow.entities.CityWeather
import br.com.andesoncfsilva.weathernow.utils.WNLog
import br.com.andesoncfsilva.weathernow.views.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cities_weather_map.*
import kotlinx.android.synthetic.main.fragment_cities_weather_map.view.*
import kotlinx.android.synthetic.main.window_layout.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CitiesWeatherMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnCameraMoveStartedListener, OnMarkerClickListener {


    private var cities = mutableMapOf<Int, CityWeather>()
    private var markers = mutableMapOf<String, Boolean>()
    private var moved = false
    private var timer: CountDownTimer? = null
    private var mMapView: MapView? = null
    private var mMap: GoogleMap? = null
    var lastOpened: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_cities_weather_map, container, false)
        retainInstance = true
        setupMap(rootView, savedInstanceState)

        timer = object : CountDownTimer(1500, 1500) {
            override fun onTick(l: Long) {
                // Not needed
            }

            override fun onFinish() {
                timer?.cancel()
                mMap?.cameraPosition?.target?.let {
                    EventBus.getDefault().post(SearchNewWeatherCities(it.latitude, it.longitude))
                }
            }
        }

        return rootView
    }

    private fun setupMap(rootView: View, savedInstanceState: Bundle?) {
        mMapView = rootView.mapView
        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume()

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            WNLog.e(e)
        }
        mMapView?.getMapAsync(this)
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
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.uiSettings?.isCompassEnabled = false
        mMap?.uiSettings?.isRotateGesturesEnabled = false
        mMap?.uiSettings?.isTiltGesturesEnabled = false
        mMap?.uiSettings?.isZoomGesturesEnabled = false
        mMap?.uiSettings?.isZoomControlsEnabled = false

        mMap?.setInfoWindowAdapter(this)
        mMap?.setOnMarkerClickListener(this)
    }

    //avoid to move camera onclick
    override fun onMarkerClick(marker: Marker?): Boolean {
        if (lastOpened != null) {
            lastOpened?.hideInfoWindow()
            if (lastOpened?.equals(marker) == true) {
                lastOpened = null
                return true
            }
        }

        marker?.showInfoWindow()
        lastOpened = marker

        return true
    }

    override fun onCameraIdle() {
        if (moved) {
            moved = false
            timer?.start()
        }
    }

    override fun onCameraMoveStarted(p0: Int) {
        moved = true
        timer?.cancel()
    }

    override fun getInfoWindow(arg0: Marker): View? {
        return null
    }

    // Defines the contents of the InfoWindow
    override fun getInfoContents(marker: Marker): View {

        val windowView = LayoutInflater.from(context).inflate(R.layout.window_layout, null)
        val latLng = marker.position

        val cityWeather = cities.values.firstOrNull { it.longitude == latLng.longitude && it.latitude == latLng.latitude }
        cityWeather?.let { bindWindowInfo(windowView, it, marker) }

        // Returning the view containing InfoWindow contents
        return windowView

    }

    private fun bindWindowInfo(windowView: View, it: CityWeather, marker: Marker) {
        windowView.tvCity.text = it.cityName
        windowView.tvTemp.text = context.getString(R.string.current_temp, it.currentTemperature.toInt().toString(), it.unit.value)
        var isImageLoaded = markers[marker.id]
        if (isImageLoaded == true) {
            Picasso.with(context)
                    .load(it.weatherImageUrl)
                    .into(windowView.ivWeather)
        } else {
            isImageLoaded = true;
            markers.put(marker.id, isImageLoaded)
            Picasso.with(context)
                    .load(it.weatherImageUrl)
                    .into(windowView.ivWeather, object : Callback {
                        override fun onSuccess() {
                            marker.showInfoWindow()
                        }

                        override fun onError() {
                        }
                    })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setCameraPosition(e: SetCameraPosition) {
        val currentLocation = LatLng(e.latitude, e.longitude)
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 9.0f))
        mMap?.setOnCameraIdleListener(this)
        mMap?.setOnCameraMoveStartedListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCities(e: ShowCitiesWeather) {
        mMap?.clear()
        cities.clear()
        e.list.forEach { c ->
            val marker = mMap?.addMarker(MarkerOptions().position(LatLng(c.latitude, c.longitude)))
            marker?.let { m ->
                markers.put(m.id, false)
                cities.put(c.id, c)
            }
        }
        lastOpened?.showInfoWindow()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showLoading(e: ShowLoading) {
        pbLoading.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun hideLoading(e: HideLoading) {
        pbLoading.visibility = View.INVISIBLE
    }

}
