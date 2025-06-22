package com.example.feature.home.impl.ui.homelist

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
import com.example.feature.home.impl.R
import com.example.feature.home.impl.ui.homedetail.HomeDetailViewModel
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
                    navController.navigate(
                        R.id.action_home_to_detail,
                        Bundle().apply {
                            putInt(
                                HomeDetailViewModel.KEY_ITEM_ID,
                                homeItem.id
                            )
                        }
                    )
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
