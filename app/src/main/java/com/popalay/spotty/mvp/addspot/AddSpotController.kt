package com.popalay.spotty.mvp.addspot

import android.app.Activity
import android.content.Intent
import android.view.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pawegio.kandroid.d
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.toPx
import com.popalay.spotty.models.Position
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.view.ElasticDragDismissFrameLayout
import com.popalay.spotty.view.changehandlers.ScaleFadeChangeHandler
import kotlinx.android.synthetic.main.controller_add_spot.view.*
import javax.inject.Inject


class AddSpotController : AddSpotView, BaseController<AddSpotView, AddSpotPresenter>() {
    private val REQUEST_PLACE_PICKER = 100

    private val MENU_ACCEPT: Int = Menu.FIRST
    @Inject lateinit var dataManager: DataManager

    private var mapView: MapView? = null

    private lateinit var selectedPlace: Place

    init {
        App.appComponent.inject(this)
        setHasOptionsMenu(true)
    }

    private val dragDismissListener = object : ElasticDragDismissFrameLayout.ElasticDragDismissCallback() {
        override fun onDragDismissed() {
            d("sdf")
            overridePopHandler(ScaleFadeChangeHandler())
            router.popCurrentController()
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_add_spot, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        (view as ElasticDragDismissFrameLayout).addListener(dragDismissListener)
        initUI(view)
    }

    override fun createPresenter() = AddSpotPresenter()

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.let {
            it.clear()
            it.add(0, MENU_ACCEPT, Menu.NONE, "Accept").setIcon(R.drawable.ic_accept)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_ACCEPT -> {
                saveSpot()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveSpot() {
        val spot: Spot = Spot()
        spot.title = view.title.text.toString().trim()
        spot.description = view.description.text.toString().trim()
        spot.address = "${selectedPlace.name}, ${selectedPlace.address}"
        spot.position = Position(selectedPlace.latLng.latitude, selectedPlace.latLng.longitude)

        dataManager.saveSpot(spot)

        router.popCurrentController()
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        (view as ElasticDragDismissFrameLayout).removeListener(dragDismissListener)
    }

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        setTitle("Spot creating")
        getSupportActionBar()?.setHomeButtonEnabled(true)
        with(view.toolbar) {
            setNavigationIcon(R.drawable.ic_clear)
            setNavigationOnClickListener {
                router.popCurrentController()
            }
        }
        view.pick_place.setOnClickListener {
            pickPlace()
        }
    }

    private fun pickPlace() {
        try {
            val intentBuilder = PlacePicker.IntentBuilder()
            val intent = intentBuilder.build(activity)
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER)
        } catch (e: GooglePlayServicesRepairableException) {
            // ...
        } catch (e: GooglePlayServicesNotAvailableException) {
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === REQUEST_PLACE_PICKER && resultCode === Activity.RESULT_OK) {
            selectedPlace = PlacePicker.getPlace(activity, data)
            view.pick_place.text = "${selectedPlace.name}, ${selectedPlace.address}"
            updateMap(selectedPlace.latLng)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateMap(latLng: LatLng) {
        if (mapView != null) {
            view.content_layout.removeView(mapView)

        }
        val mapOptions = GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .liteMode(true)
                .camera(CameraPosition.fromLatLngZoom(latLng, 15f))
        mapView = MapView(activity, mapOptions)
        mapView?.let {
            view.content_layout.addView(it, 1)
            it.layoutParams.height = 200.toPx()
            it.onCreate(args)
            it.getMapAsync {
                it.addMarker(MarkerOptions().position(latLng))
            }
        }
    }
}