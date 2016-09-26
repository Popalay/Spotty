package com.popalay.spotty.controllers

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.loadImg
import kotlinx.android.synthetic.main.activity_main2.view.*
import kotlinx.android.synthetic.main.controller_home.view.*
import kotlinx.android.synthetic.main.header_drawer.view.*
import javax.inject.Inject

class HomeController : BaseController(), Drawer.OnDrawerItemClickListener {

    private val DEFAULT_DRAWER_POSITION = -1L

    @Inject lateinit var authManager: dagger.Lazy<AuthManager>
    @Inject lateinit var dataManager: DataManager

    private lateinit var itemMap: PrimaryDrawerItem
    private lateinit var itemLikedSpots: PrimaryDrawerItem
    private lateinit var itemMySpots: PrimaryDrawerItem
    private lateinit var drawer: Drawer
    private lateinit var childRouter: Router

    private lateinit var toggle: ActionBarDrawerToggle

    init {
        App.sessionComponent.inject(this)
        setHasOptionsMenu(true)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.activity_main2, false)
        return view
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

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
        toggle = ActionBarDrawerToggle(activity, view.drawer_layout, R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        view.drawer_layout.addDrawerListener(object:DrawerLayout.DrawerListener{
            override fun onDrawerClosed(drawerView: View?) {

            }

            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View?) {

            }

        })
        toggle.syncState()

        /*itemMap = PrimaryDrawerItem().withName("Map").withIcon(R.drawable.ic_map)
        itemLikedSpots = PrimaryDrawerItem().withName("Liked Spots").withIcon(R.drawable.ic_heart)
        itemMySpots = PrimaryDrawerItem().withName("My Spots").withIcon(R.drawable.ic_my)
        drawer = DrawerBuilder().withActivity(activity)
                //.withToolbar(view.toolbar)
                //.withActionBarDrawerToggle(true)
                //.withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.header_drawer)
                .addDrawerItems(itemMap, itemLikedSpots, itemMySpots)
                .withOnDrawerItemClickListener(this)
                .withStickyFooterDivider(false)
                .withStickyFooter(R.layout.footer_drawer)
                .withSelectedItem(DEFAULT_DRAWER_POSITION)
                .build()

        drawer.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        setUserInfo()

        drawer.stickyFooter.logout.setOnClickListener {
            signOut()
        }*/
    }

    private fun signOut() {
        authManager.get().signOut()
        drawer.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        router.setRoot(RouterTransaction.with(LoginController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }

    private fun setUserInfo() {
        dataManager.getCurrentUser()?.let {
            drawer.header.display_name.text = it.displayName
            drawer.header.image_profile.loadImg(it.photoUrl?.toString())
        }
    }

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
        setController(getControllerByPosition(position.toLong()))
        drawer.closeDrawer()
        return true
    }

    fun setController(controller: BaseController) {
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

    fun getControllerByPosition(position: Long): BaseController {
        return when (position.toInt()) {
            -1 -> DashboardController()
            else -> DashboardController()
        }
    }

}
