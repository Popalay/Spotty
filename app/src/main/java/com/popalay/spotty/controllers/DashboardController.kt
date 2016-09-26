package com.popalay.spotty.controllers

import android.location.Location
import android.os.Bundle
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.location.LocationManager
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.controller_dashboard.view.*
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class DashboardController : BaseController {

    @Inject lateinit var locationManager: LocationManager

    private val MENU_ADD: Int = Menu.FIRST
    private val MENU_SEARCH: Int = MENU_ADD + 1

    init {
        App.appComponent.inject(this)
    }

    constructor() : super() {
        setHasOptionsMenu(true)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_dashboard, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    private fun initUI(view: View) {
        view.map_view.onCreate(args)
        RxPermissions.getInstance(activity)
                .request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        view.map_view.getMapAsync {
                            initMap(it)
                        }
                    } else {
                        // Oups permission denied
                    }
                })
    }

    private fun initMap(map: GoogleMap) {
        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true
        try {
            MapsInitializer.initialize(this.activity)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        showLastLocation(map)
    }

    private fun showLastLocation(map: GoogleMap) {
        locationManager.getLastLocation()
                .compose(bindUntilEvent<Location>(ControllerEvent.DETACH))
                .filter { it != null }
                .doOnNext { it.toString() }
                .map { LatLng(it.latitude, it.longitude) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it, 10f)
                    map.animateCamera(cameraUpdate)
                }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        view.map_view.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        view.map_view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        view.map_view.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view.map_view.onSaveInstanceState(outState)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.let {
            it.clear()
            it.add(0, MENU_ADD, Menu.NONE, "Add").setIcon(R.drawable.ic_add)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            it.add(0, MENU_SEARCH, Menu.NONE, "Search").setIcon(R.drawable.ic_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_ADD -> {
                addSpot()
                return true
            }
            MENU_SEARCH -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addSpot() {
        parentController.childRouters[0].pushController(RouterTransaction.with(AddSpotController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))


        /* try {
             val intentBuilder = PlacePicker.IntentBuilder()
             val intent = intentBuilder.build(parentController.activity)
             // Start the intent by requesting a result,
             // identified by a request code.
             parentController.activity.startActivityForResult(intent, 101)
         } catch (e: GooglePlayServicesRepairableException) {
             // ...
         } catch (e: GooglePlayServicesNotAvailableException) {
             // ...
         }*/
    }

}