package com.popalay.spotty.ui.home

import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.popalay.spotty.R
import com.popalay.spotty.models.User
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.ui.dashboard.DashboardController
import com.popalay.spotty.ui.login.LoginController
import com.popalay.spotty.ui.spotsmap.SpotsMapController
import com.popalay.spotty.utils.extensions.inflate
import kotlinx.android.synthetic.main.content_home.view.*
import kotlinx.android.synthetic.main.controller_home.view.*
import kotlinx.android.synthetic.main.footer_drawer.view.*
import kotlinx.android.synthetic.main.header_drawer.view.*

class HomeController : HomeView, BaseController<HomeView, HomePresenter>(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var childRouter: Router
    private lateinit var toggle: ActionBarDrawerToggle

    init {
        setHasOptionsMenu(true)
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_home, false)
        return view
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    override fun createPresenter() = HomePresenter()

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        setTitle(R.string.app_name)
        setDefaultController(view)
        initDrawer(view)
    }

    private fun setDefaultController(view: View) {
        childRouter = getChildRouter(view.home_container, null)
        if (!childRouter.hasRootController()) {
            openDashboard()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    private fun initDrawer(view: View) {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(activity, view.drawer_layout, view.toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        view.drawer_layout.addDrawerListener(toggle)
        view.nav_view.setNavigationItemSelectedListener(this)
        toggle.syncState()

        view.logout.setOnClickListener {
            presenter.signOut()
        }
    }

    override fun startSignIn() {
        router.setRoot(RouterTransaction.with(LoginController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }

    override fun setUserInfo(user: User) {
        view.nav_view.getHeaderView(0).display_name.text = user.displayName
        view.nav_view.getHeaderView(0).image_profile.setImageURI(user.profilePhoto, activity)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        presenter.open(item.itemId)
        closeDrawer()
        return true
    }

    private fun closeDrawer() {
        view.drawer_layout.closeDrawers()
    }

    override fun openDashboard() {
        childRouter.setRoot(RouterTransaction.with(DashboardController()))
    }

    override fun openMap() {
        childRouter.setRoot(RouterTransaction.with(SpotsMapController()))
    }

    override fun openLikedSpots() {
    }

    override fun openMySpots() {
    }
}
