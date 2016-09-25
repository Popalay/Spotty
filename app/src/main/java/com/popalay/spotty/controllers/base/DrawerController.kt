package com.popalay.spotty.controllers.base

import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.controllers.DashboardController
import com.popalay.spotty.controllers.LoginController
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.extensions.loadImg
import kotlinx.android.synthetic.main.footer_drawer.view.*
import kotlinx.android.synthetic.main.header_drawer.view.*
import javax.inject.Inject

abstract class DrawerController : BaseController(), Drawer.OnDrawerItemClickListener {

    @Inject lateinit var authManager: dagger.Lazy<AuthManager>
    @Inject lateinit var dataManager: DataManager

    private lateinit var itemMap: PrimaryDrawerItem
    private lateinit var itemLikedSpots: PrimaryDrawerItem
    private lateinit var itemMySpots: PrimaryDrawerItem
    private lateinit var drawer: Drawer
    private lateinit var childRouter: Router

    private val DEFAULT_DRAWER_POSITION = -1L

    @CallSuper
    override fun onAttach(view: View) {
        super.onAttach(view)
        initUI()
    }

    private fun initUI() {
        setSupportActionBar(provideToolbar())
        setDefaultController()
        initDrawer()
    }

    private fun setDefaultController() {
        @IdRes val frameId = resources.getIdentifier("home_container", "id", activity.packageName)
        val container = view.findViewById(frameId) as ViewGroup
        childRouter = getChildRouter(container, null)
        if (!childRouter.hasRootController()) {
            childRouter.setRoot(RouterTransaction.with(DashboardController()))
        }
        childRouter.setPopsLastView(true)
    }

    private fun initDrawer() {
        itemMap = PrimaryDrawerItem().withName("Map").withIcon(R.drawable.ic_map)
        itemLikedSpots = PrimaryDrawerItem().withName("Liked Spots").withIcon(R.drawable.ic_heart)
        itemMySpots = PrimaryDrawerItem().withName("My Spots").withIcon(R.drawable.ic_my)
        drawer = DrawerBuilder().withActivity(activity).withToolbar(provideToolbar())
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
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
        }
    }

    private fun signOut() {
        authManager.get().signOut()
        drawer.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        router.replaceTopController(RouterTransaction.with(LoginController())
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

    protected abstract fun getControllerByPosition(position: Long): BaseController

    protected abstract fun provideToolbar(): Toolbar

    protected abstract fun getContainer(): ViewGroup

}