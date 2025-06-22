package com.example.app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.app.databinding.ActivityMainBinding
import com.example.core.navigation.BottomNavEntry
import com.example.core.navigation.FeatureEntry
import com.example.core.navigation.OnBackPressedHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var bottomNavEntries: Set<@JvmSuppressWildcards BottomNavEntry>

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private val tabFragments = mutableMapOf<String, Fragment>()
    private var currentTab: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        setupBackHandler()

        if (savedInstanceState == null) {
            val initialTab = bottomNavEntries.minByOrNull { it.bottomNavPosition() }
            initialTab?.let { switchToTab(it) }
        }
    }

    private fun setupBottomNavigation() {
        val sortedTabs = bottomNavEntries.sortedBy { it.bottomNavPosition() }
        binding.bottomNav.menu.clear()

        for (entry in sortedTabs) {
            binding.bottomNav.menu.add(
                0,
                entry.route().hashCode(),
                entry.bottomNavPosition(),
                entry.label()
            ).setIcon(entry.iconRes())
        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            val selectedRoute = bottomNavEntries.firstOrNull {
                it.route().hashCode() == menuItem.itemId
            }?.route() ?: return@setOnItemSelectedListener false

            if (currentTab != selectedRoute) {
                val entry = bottomNavEntries.first { it.route() == selectedRoute }
                switchToTab(entry)
                true
            } else {
                false
            }
        }
    }

    private fun switchToTab(entry: BottomNavEntry) {
        val transaction = supportFragmentManager.beginTransaction()
        val tag = entry.route()

        val newFragment = tabFragments.getOrPut(tag) {
            entry.createRootFragment()
        }

        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        if (!newFragment.isAdded) {
            transaction.add(R.id.fragment_container, newFragment, tag)
        }
        transaction.show(newFragment).commitNowAllowingStateLoss()
        currentTab = tag
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val current = supportFragmentManager.findFragmentByTag(currentTab)
                val handled = (current as? OnBackPressedHandler)?.onBackPressed() ?: false
                if (!handled) {
                    finish()
                }
            }
        })
    }
}

