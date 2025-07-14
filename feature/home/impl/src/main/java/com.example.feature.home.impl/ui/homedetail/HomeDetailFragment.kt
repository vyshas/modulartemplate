package com.example.feature.home.impl.ui.homedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDetailFragment : Fragment() {

    private val viewModel: HomeDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeDetailScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        val navController = try {
                            findNavController()
                        } catch (e: IllegalStateException) {
                            null
                        }

                        if (navController?.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    },
                )
            }
        }
    }
}
