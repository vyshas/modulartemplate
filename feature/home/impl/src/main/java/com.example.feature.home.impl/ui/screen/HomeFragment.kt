package com.example.feature.home.impl.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.core.navigation.OnBackPressedHandler
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.impl.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnBackPressedHandler {

    private val viewModel: HomeViewModel by viewModels()
    private val navController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeContent(viewModel) { homeItem: HomeItem ->

                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (navController.popBackStack().not()) {
            false // not handled
        } else {
            true // handled
        }
    }
}

@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    onItemClick: (HomeItem) -> Unit
) {
    HomeScreen(viewModel = viewModel, onItemClick = onItemClick)
}
