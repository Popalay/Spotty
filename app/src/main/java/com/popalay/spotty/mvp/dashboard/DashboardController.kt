package com.popalay.spotty.mvp.dashboard

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.popalay.spotty.R
import com.popalay.spotty.adapters.SpotAdapter
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.addspot.AddSpotController
import com.popalay.spotty.mvp.base.BaseController
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.controller_dashboard.view.*


class DashboardController : DashboardView, BaseController<DashboardView, DashboardPresenter>() {

    private val MENU_ADD: Int = Menu.FIRST
    private val MENU_SEARCH: Int = MENU_ADD + 1

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    private val spotAdapter: SpotAdapter = SpotAdapter()

    init {
        setHasOptionsMenu(true)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_dashboard, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        mapView = view.map_view
        mapView.onCreate(args)
    }

    override fun createPresenter() = DashboardPresenter()

    private fun initUI(view: View) {
        initList(view)
        RxPermissions.getInstance(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        mapView.getMapAsync {
                            map = it
                            initMap()
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
            presenter.loadData()
        }
    }

    override fun setData(data: List<Spot>) {
        spotAdapter.items = data.toMutableList()
        spotAdapter.notifyDataSetChanged()
    }

    private fun initMap() {
        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true
        try {
            MapsInitializer.initialize(this.activity)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        presenter.getLastLocation()
    }

    override fun setLocation(position: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15f)
        map.animateCamera(cameraUpdate)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initUI(view)
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
                presenter.addSpot()
                true
            }
            MENU_SEARCH -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun startAddSpot() {
        parentController.router.pushController(RouterTransaction.with(AddSpotController())
                .popChangeHandler(VerticalChangeHandler())
                .pushChangeHandler(VerticalChangeHandler()))
    }

}