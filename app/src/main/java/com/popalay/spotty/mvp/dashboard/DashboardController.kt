package com.popalay.spotty.mvp.dashboard

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.adapters.SpotAdapter
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.location.LocationManager
import com.popalay.spotty.mvp.addspot.AddSpotController
import com.popalay.spotty.mvp.base.BaseController
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.controller_dashboard.view.*
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class DashboardController : BaseController<DashboardView, DashboardPresenter> {
    @Inject lateinit var locationManager: LocationManager

    @Inject lateinit var dataManager: DataManager
    private val MENU_ADD: Int = Menu.FIRST


    private val MENU_SEARCH: Int = MENU_ADD + 1
    private lateinit var mapView: MapView

    private val spotAdapter: SpotAdapter = SpotAdapter()

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

    override fun createPresenter() = DashboardPresenter()

    private fun initUI(view: View) {
        initList(view)
        mapView = view.map_view
        mapView.onCreate(args)
        RxPermissions.getInstance(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        mapView.getMapAsync {
                            initMap(it)
                        }
                    } else {
                        // Oups permission denied
                    }
                })
    }

    private fun initList(view: View) {
        with(view.recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = spotAdapter
            loadData()
        }
    }

    private fun loadData() {
        dataManager.getSpots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    spotAdapter.items = it
                    spotAdapter.notifyDataSetChanged()
                }
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
/* todo       locationManager.getLastLocation()
                .compose(bindUntilEvent<Location>(ControllerEvent.DETACH))
                .filter { it != null }
                .doOnNext { it.toString() }
                .map { LatLng(it.latitude, it.longitude) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it, 10f)
                    map.animateCamera(cameraUpdate)
                }*/
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        mapView.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
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
                true
            }
            MENU_SEARCH -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addSpot() {
        parentController.router.pushController(RouterTransaction.with(AddSpotController())
                .popChangeHandler(VerticalChangeHandler(false))
                .pushChangeHandler(VerticalChangeHandler()))
    }

}