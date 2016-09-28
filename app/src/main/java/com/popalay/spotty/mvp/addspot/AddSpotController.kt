package com.popalay.spotty.mvp.addspot

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
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
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.adapters.AddSpotPhotosAdapter
import com.popalay.spotty.data.ImageManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.toPx
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.ui.ElasticDragDismissFrameLayout
import com.popalay.spotty.ui.changehandlers.ScaleFadeChangeHandler
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport
import kotlinx.android.synthetic.main.controller_add_spot.view.*
import javax.inject.Inject


class AddSpotController : AddSpotView, BaseController<AddSpotView, AddSpotPresenter>() {

    private val REQUEST_PLACE_PICKER = 100

    private val MENU_ACCEPT: Int = Menu.FIRST

    @Inject lateinit var imageManager: ImageManager
    private lateinit var addPhotosAdapter: AddSpotPhotosAdapter

    private var mapView: MapView? = null

    init {
        App.appComponent.inject(this)
        setHasOptionsMenu(true)
    }

    private val dragDismissListener = object : ElasticDragDismissFrameLayout.ElasticDragDismissCallback() {
        override fun onDragDismissed() {
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
        presenter.saveSpot(spot)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        (view as ElasticDragDismissFrameLayout).removeListener(dragDismissListener)
    }

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        setTitle("Spot creating")
        getSupportActionBar()?.setHomeButtonEnabled(true)
        initList(view)
        with(view.toolbar) {
            setNavigationIcon(R.drawable.ic_clear)
            setNavigationOnClickListener {
                router.popCurrentController()
            }
        }
        view.pick_place.setOnClickListener {
            presenter.pickPlace()
        }
    }

    private fun initList(view: View) {
        with(view.recycler) {
            layoutManager = StaggeredGridLayoutManager(3, OrientationHelper.HORIZONTAL)
            setHasFixedSize(true)
            addPhotosAdapter = AddSpotPhotosAdapter()
            adapter = addPhotosAdapter
            RecyclerItemClickSupport.addTo(this).setOnItemClickListener { recyclerView, i, view ->
                if (i == 0) {
                    openChoosePhotoDialog()
                }
            }
        }
    }

    private fun openChoosePhotoDialog() {
        BottomSheetBuilder(activity, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.add_photo_dialog)
                .setItemClickListener {
                    when (it.itemId) {
                        R.id.gallery -> imageManager.pickPhoto()
                        R.id.camera -> imageManager.takePhoto()
                    }
                }.createDialog().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === REQUEST_PLACE_PICKER && resultCode === Activity.RESULT_OK) {
            presenter.setPickedPlace(PlacePicker.getPlace(activity, data))
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
                .camera(CameraPosition.fromLatLngZoom(latLng, 16f))
        mapView = MapView(activity, mapOptions)
        mapView?.let {
            view.content_layout.addView(it, 1)
            it.layoutParams.height = 200.toPx()
            it.onCreate(args)
            it.getMapAsync { it.addMarker(MarkerOptions().position(latLng)) }
        }
    }

    override fun showError(message: String) {
        showSnackbar(message)
    }

    override fun onSpotSaved() {
        router.popCurrentController()
    }

    override fun pickPlace() {
        try {
            val intentBuilder = PlacePicker.IntentBuilder()
            val intent = intentBuilder.build(activity)
            startActivityForResult(intent, REQUEST_PLACE_PICKER)
        } catch (e: GooglePlayServicesRepairableException) {
        } catch (e: GooglePlayServicesNotAvailableException) {
        }
    }

    override fun showPickedPlace(place: Place) {
        view.pick_place.text = "${place.name}, ${place.address}"
        updateMap(place.latLng)
    }
}