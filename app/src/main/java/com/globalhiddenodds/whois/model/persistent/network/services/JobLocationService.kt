package com.globalhiddenodds.whois.model.persistent.network.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.location.LocationManager
import com.globalhiddenodds.whois.model.persistent.network.LocationListener

class JobLocationService: JobService() {
    companion object {
        private const val locationInterval = 3000
        private const val locationDistance = 5f
    }

    internal var context: Context? = null
    private var locationManager: LocationManager? = null

    private var locationListeners: Array<LocationListener>? = null


    override fun onCreate() {
        super.onCreate()
        context = applicationContext!!
        locationListeners = arrayOf(LocationListener(
            LocationManager
            .GPS_PROVIDER, context!!),
            LocationListener(LocationManager.NETWORK_PROVIDER, context!!))

    }

    override fun onStopJob(params: JobParameters?): Boolean {
        if (locationManager != null) {
            for (i in locationListeners!!.indices) {
                try {
                    locationManager!!.removeUpdates(locationListeners!![i])
                } catch (ex: Exception) {
                    println("Fail to remove location listeners, ignore: $ex")
                }

            }
        }
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        initLocation()
        return true
    }

    private fun initLocation(){
        initializeLocationManager()

        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                locationInterval.toLong(), locationDistance,
                locationListeners!![1])

        } catch (ex: SecurityException) {
            println("fail to request location update, ignore $ex")
        } catch (ex: IllegalArgumentException) {
            println("network provider does not exist, ${ex.message}")
        }

        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                locationInterval.toLong(), locationDistance,
                locationListeners!![0])
        } catch (ex: SecurityException) {
            println("fail to request location update, ignore: $ex")
        } catch (ex: IllegalArgumentException) {
            println("gps provider does not exist ${ex.message}")
        }
    }

    private fun initializeLocationManager() {
        println("initializeLocationManager")
        if (locationManager == null) {
            locationManager = applicationContext
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }
}