package com.popalay.spotty.controllers.base

import android.support.annotation.CallSuper
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.pawegio.kandroid.d
import com.popalay.spotty.R
import com.popalay.spotty.extensions.loadImg
import kotlinx.android.synthetic.main.header_drawer.view.*

abstract class DrawerController : BaseController() {

    private lateinit var itemMap: PrimaryDrawerItem
    private lateinit var itemLikedSpots: PrimaryDrawerItem
    private lateinit var itemMySpots: PrimaryDrawerItem
    private lateinit var drawer: Drawer

    @CallSuper
    override fun onAttach(view: View) {
        super.onAttach(view)
        initDrawer()
    }


    private fun initDrawer() {
        d("initDrawer: ")
        itemMap = PrimaryDrawerItem().withName("Map").withIcon(R.drawable.ic_map)
        itemLikedSpots = PrimaryDrawerItem().withName("Liked Spots").withIcon(R.drawable.ic_heart)
        itemMySpots = PrimaryDrawerItem().withName("My Spots").withIcon(R.drawable.ic_my)
        drawer = DrawerBuilder().withActivity(activity).withToolbar(provideToolbar()).withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.header_drawer)
                .addDrawerItems(itemMap, itemLikedSpots, itemMySpots)
                //.withOnDrawerItemClickListener()
                //.withFooter(R.layout.drawer_footer)
                .withFooterDivider(false)
                .build()
        setUserInfo()
        /*val footer = mDrawer.getFooter()
        mBtnLogout = footer.findViewById(R.id.btnLogout) as Button
        mBtnLogout.setOnClickListener({ v -> logout() })
        choseSelectedItem()*/
    }

    private fun setUserInfo(){
        FirebaseAuth.getInstance().currentUser?.let {
            drawer.header.display_name.text = it.displayName
            drawer.header.image_profile.loadImg(it.photoUrl?.toString())
        }
    }

    protected abstract fun provideToolbar(): Toolbar

}