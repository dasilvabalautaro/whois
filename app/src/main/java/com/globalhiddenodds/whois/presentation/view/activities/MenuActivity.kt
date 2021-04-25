package com.globalhiddenodds.whois.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.model.observer.LocationChangeObserver
import com.globalhiddenodds.whois.model.persistent.caching.Variables
import com.globalhiddenodds.whois.model.persistent.network.ManagerLocation
import com.globalhiddenodds.whois.presentation.navigation.Navigator
import com.globalhiddenodds.whois.presentation.plataform.BaseActivity
import com.globalhiddenodds.whois.presentation.view.fragments.CapturedFragment
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MenuActivity: BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context,
            MenuActivity::class.java)
    }

    @Inject
    internal lateinit var navigator: Navigator
    @Inject
    lateinit var managerLocation: ManagerLocation
    @Inject
    lateinit var locationChangeObserver: LocationChangeObserver

    override fun fragment() = CapturedFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        enablePermissions.permissionServiceLocation(this)
        if (!managerLocation.isJobServiceOn()){
            if (!managerLocation.start()){
                println("Service Location not run.")
            }
        }
        val hearLocation = locationChangeObserver.observableLocation.map { l -> l }
        disposable.add(hearLocation.observeOn(Schedulers.newThread())
            .subscribe { l ->
                run {
                    Variables.locationUser.lat= l.latitude
                    Variables.locationUser.lon = l.longitude


                }
            })
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onStart() {
        super.onStart()
        if (!Variables.isServerUp){
            checkRESTServer(whoIsWebServices)
        }
    }
    override fun onResume() {
        super.onResume()
        setOrientation(true)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.size_50){
            FaceVideoActivity.sizeFace = 0.5f
        }
        if (id == R.id.size_40){
            FaceVideoActivity.sizeFace = 0.4f
        }
        if (id == R.id.size_30){
            FaceVideoActivity.sizeFace = 0.3f
        }
        if (id == R.id.size_20){
            FaceVideoActivity.sizeFace = 0.2f
        }
        if (id == R.id.option_java){
            FaceVideoActivity.optionDetector = 0
        }
        if (id == R.id.option_native){
            FaceVideoActivity.optionDetector = 1
        }
        if (id == R.id.action_capture){
            CapturedFragment.deleteRecycler()
            navigator.showFaceVideo(this)
        }
        if (id == android.R.id.home){
            finish()
        }
        if (id == R.id.action_delete){
            CapturedFragment.deleteRecycler()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        managerLocation.cancel()
        disposable.dispose()
        subscribe.dispose()
        super.onDestroy()

    }

    private fun setOrientation(option: Boolean){
        requestedOrientation = if (option){
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else{
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}