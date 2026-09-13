package com.example.feature.home.impl.navigation

import android.content.Context
import com.example.feature.home.api.HomeDestination
import com.example.feature.home.impl.ui.homedetail.HomeDetailActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class HomeNavigatorImplTest {

    private val navigator = HomeNavigatorImpl()

    @Test
    fun `intentFor Detail creates correct Intent`() {
        val context = RuntimeEnvironment.getApplication()
        val destination = HomeDestination.Detail(homeId = 42)

        val intent = navigator.intentFor(context, destination)

        assertEquals(HomeDetailActivity::class.java.name, intent.component?.className)
        assertEquals(42, intent.getIntExtra(HomeDetailActivity.KEY_ITEM_ID, -1))
    }
}
