package com.example.feature.recipe.impl.navigation

import com.example.feature.recipe.api.RecipeDestination
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class RecipeNavigatorImplTest {

    private val navigator = RecipeNavigatorImpl()

    @Test
    fun `intentFor Recipe returns intent`() {
        val context = RuntimeEnvironment.getApplication()
        val destination = RecipeDestination.Detail(1)

        val intent = navigator.intentFor(context, destination)

        assertNotNull(intent)
    }
}
