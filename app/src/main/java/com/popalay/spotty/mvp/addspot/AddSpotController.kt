package com.popalay.spotty.mvp.addspot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
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
import com.jakewharton.rxbinding.widget.RxTextView
import com.popalay.spotty.App
import com.popalay.spotty.MAX_PHOTOS_COUNT
import com.popalay.spotty.R
import com.popalay.spotty.adapters.AddSpotPhotosAdapter
import com.popalay.spotty.data.ImageManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.toPx
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.ui.ElasticDragDismissFrameLayout
import com.popalay.spotty.ui.changehandlers.ScaleFadeChangeHandler
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport
import com.tbruyelle.rxpermissions.RxPermissions
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.controller_add_spot.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
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

    override fun onAttach(view: View) {
        super.onAttach(view)
        setListeners(view)
    }

    private fun setListeners(view: View) {
        with(view) {
            pick_place.setOnClickListener {
                presenter.pickPlace()
            }
            RxTextView.afterTextChangeEvents(title)
                    .debounce(750, TimeUnit.MILLISECONDS)
                    .map { it.editable().trim().toString() }
                    .subscribe { presenter.setTitle(it) }
            RxTextView.afterTextChangeEvents(description)
                    .debounce(750, TimeUnit.MILLISECONDS)
                    .map { it.editable().trim().toString() }
                    .subscribe { presenter.setDescription(it) }
        }
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
                presenter.saveSpot()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        with(view) {
            toolbar.setNavigationIcon(R.drawable.ic_clear)
            toolbar.setNavigationOnClickListener {
                router.popCurrentController()
            }
        }
    }

    private fun initList(view: View) {
        with(view.recycler) {
            layoutManager = GridLayoutManager(activity, 3)
            setHasFixedSize(true)
            addPhotosAdapter = AddSpotPhotosAdapter()
            adapter = addPhotosAdapter
            view.recycler.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity)
                    .sizeResId(R.dimen.small)
                    .color(0)
                    .build())
            addPhotosAdapter.onRemoveItem = { presenter.removePhoto(it) }
            RecyclerItemClickSupport.addTo(this).setOnItemClickListener { recyclerView, i, view ->
                if (i == addPhotosAdapter.getDataSize()) {
                    presenter.requestAddPhoto()
                }
            }
        }
    }

    override fun choosePhoto() {
        RxPermissions.getInstance(activity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        BottomSheetBuilder(activity, R.style.AppTheme_BottomSheetDialog)
                                .setMode(BottomSheetBuilder.MODE_LIST)
                                .setMenu(R.menu.add_photo_dialog)
                                .setItemClickListener { getPhoto(it.itemId == R.id.camera) }
                                .createDialog().show()
                    } else {
                        // Oups permission denied
                    }
                }
    }

    private fun getPhoto(useCamera: Boolean) {
        val observableUri = if (useCamera) imageManager.takePhoto() else imageManager.pickPhoto()
        observableUri.observeOn(AndroidSchedulers.mainThread())
                .subscribe { presenter.addPhoto(it) }
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
            it.setPadding(0, 0, 0, 8.toPx())
            view.content_layout.addView(it, 1)
            it.layoutParams.height = 200.toPx()
            it.onCreate(args)
            it.getMapAsync { map ->
                map.uiSettings.isMapToolbarEnabled = false
                map.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun updatePhotosCount(count: Int) {
        view.photos_count.text = "${addPhotosAdapter.getDataSize()}/$MAX_PHOTOS_COUNT"
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

    override fun removePhoto(photo: Uri) {
        addPhotosAdapter.remove(photo)
    }

    override fun addPhoto(photo: Uri) {
        addPhotosAdapter.add(photo)
    }
}