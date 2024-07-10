package com.rogoz208.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.navigation_api.Screens
import com.rogoz208.newsapp.databinding.ActivityMainBinding
import com.rogoz208.newsapp.di.ActivityComponent
import com.rogoz208.newsapp.di.ActivityModule
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.customlifecycle.StoredComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var router: IRouter

    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val bottomNavigationFragments = mapOf(
        R.id.bottom_menu_item_headlines to Screens.HeadlinesScreen,
        R.id.bottom_menu_item_saved to Screens.SavedScreen,
        R.id.bottom_menu_item_sources to Screens.SourcesScreen
    )

    private var storedComponent: StoredComponent<ActivityComponent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initStoredComponent()
        super.onCreate(savedInstanceState)

        initBottomNavigation(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(appNavigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onDestroy() {
        storedComponent?.lifecycle?.destroy()
        super.onDestroy()
    }

    private fun initStoredComponent() {
        storedComponent = XInjectionManager.bindComponentToCustomLifecycle(object :
            IHasComponent<ActivityComponent> {
            override fun getComponent(): ActivityComponent =
                (applicationContext as App).applicationComponent.plus(ActivityModule(this@MainActivity))
        })
        storedComponent?.component?.inject(this)
    }

    private fun initBottomNavigation(savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            router.newRootScreen(bottomNavigationFragments[item.itemId]!!)
            true
        }
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.bottom_menu_item_headlines
        }
    }
}