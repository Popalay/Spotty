package com.popalay.spotty.controllers

import android.view.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate


class DashboardController : BaseController {


    private val MENU_ADD: Int = Menu.FIRST
    private val MENU_SEARCH: Int = MENU_ADD + 1

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

    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.let {
            it.clear()
            it.add(0, MENU_ADD, Menu.NONE, "Add").setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            it.add(0, MENU_SEARCH, Menu.NONE, "Search").setIcon(R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
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
        try {
            val intentBuilder = PlacePicker.IntentBuilder()
            val intent = intentBuilder.build(parentController.activity)
            // Start the intent by requesting a result,
            // identified by a request code.
            parentController.activity.startActivityForResult(intent, 101)
        } catch (e: GooglePlayServicesRepairableException) {
            // ...
        } catch (e: GooglePlayServicesNotAvailableException) {
            // ...
        }
    }

}