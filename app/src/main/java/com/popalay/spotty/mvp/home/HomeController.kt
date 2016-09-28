package com.popalay.spotty.mvp.home

import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.loadInCircle
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.mvp.dashboard.DashboardController
import com.popalay.spotty.mvp.login.LoginController
import dagger.Lazy
import kotlinx.android.synthetic.main.content_home.view.*
import kotlinx.android.synthetic.main.controller_home.view.*
import kotlinx.android.synthetic.main.footer_drawer.view.*
import kotlinx.android.synthetic.main.header_drawer.view.*
import javax.inject.Inject

class HomeController : HomeView, BaseController<HomeView, HomePresenter>(), Drawer.OnDrawerItemClickListener {

    @Inject lateinit var authManager: Lazy<AuthManager>

    @Inject lateinit var dataManager: DataManager
    private lateinit var childRouter: Router

    private lateinit var toggle: ActionBarDrawerToggle

    init {
        App.sessionComponent.inject(this)
        setHasOptionsMenu(true)
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
        setTitle(activity.getString(R.string.app_name))
        setDefaultController(view)
        initDrawer(view)
    }

    private fun setDefaultController(view: View) {
        childRouter = getChildRouter(view.home_container, null)
        if (!childRouter.hasRootController()) {
            childRouter.setRoot(RouterTransaction.with(DashboardController()))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    private fun initDrawer(view: View) {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toggle = ActionBarDrawerToggle(activity, view.drawer_layout, view.toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        view.drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setUserInfo(view)

        view.logout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        authManager.get().signOut()
        router.setRoot(RouterTransaction.with(LoginController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }

    private fun setUserInfo(view: View) {
        dataManager.getCurrentUser()?.let {
            view.nav_view.getHeaderView(0).display_name.text = it.displayName
            view.nav_view.getHeaderView(0).image_profile.loadInCircle(it.photoUrl?.toString())
        }
    }

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
        setController(getControllerByPosition(position.toLong()))
        closeDrawer()
        return true
    }

    private fun closeDrawer() {
        view.drawer_layout.closeDrawers()
    }

    fun setController(controller: Controller) {
        if (childRouter.backstackSize > 0) {
            childRouter.replaceTopController(RouterTransaction.with(controller)
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))
        } else {
            childRouter.pushController(RouterTransaction.with(controller)
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))
        }
    }

    fun getControllerByPosition(position: Long): Controller {
        return when (position.toInt()) {
            -1 -> DashboardController()
            else -> DashboardController()
        }
    }

}
