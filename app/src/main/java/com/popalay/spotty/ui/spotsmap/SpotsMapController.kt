package com.popalay.spotty.ui.spotsmap

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.popalay.spotty.R
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.utils.extensions.inflate
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.controller_spots_map.view.*


class SpotsMapController : SpotsMapView, BaseController<SpotsMapView, SpotsMapPresenter>() {

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_spots_map, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        mapView = view.mapView
        mapView.onCreate(args)
    }

    override fun createPresenter() = SpotsMapPresenter()

    private fun initUI(view: View) {
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

    private fun initMap() {
        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true
        try {
            MapsInitializer.initialize(this.activity)
            presenter.getLastLocation()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
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

    override fun showMarker(spot: Spot) {
        map.addMarker(MarkerOptions()
                .position(spot.position.toLatLng())
                .title(spot.title))
    }
}