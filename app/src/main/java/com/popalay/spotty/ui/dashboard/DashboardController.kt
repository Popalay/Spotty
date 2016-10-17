package com.popalay.spotty.ui.dashboard

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.popalay.spotty.R
import com.popalay.spotty.adapters.SpotsAdapter
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.addspot.AddSpotController
import com.popalay.spotty.ui.base.BaseLceController
import com.popalay.spotty.ui.spotdetails.SpotDetailsController
import com.popalay.spotty.utils.extensions.inflate
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport
import kotlinx.android.synthetic.main.controller_dashboard.view.*


class DashboardController : DashboardView, BaseLceController<RecyclerView, List<Spot>, DashboardView, DashboardPresenter>() {

    private val MENU_ADD: Int = Menu.FIRST
    private val MENU_SEARCH: Int = MENU_ADD + 1

    private val spotAdapter: SpotsAdapter = SpotsAdapter()

    init {
        setHasOptionsMenu(true)
        retainViewMode = RetainViewMode.RETAIN_DETACH
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
    }

    private fun initList(view: View) {
        with(view.contentView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = spotAdapter
            RecyclerItemClickSupport.addTo(this).setOnItemClickListener { recyclerView, i, view ->
                presenter.openSpot(spotAdapter.items[i])
            }
        }
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

    override fun openSpot(spot: Spot) {
        parentController.router.pushController(RouterTransaction.with(SpotDetailsController(spot.id))
                .popChangeHandler(VerticalChangeHandler())
                .pushChangeHandler(VerticalChangeHandler()))
    }

    override fun setData(data: List<Spot>) {
        spotAdapter.items = data.toMutableList()
        spotAdapter.notifyDataSetChanged()
    }

    override fun loadData(pullToRefresh: Boolean) {
    }

    override fun getErrorMessage(e: Throwable, pullToRefresh: Boolean): String? {
        return e.message
    }
}