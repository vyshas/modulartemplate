package com.example.feature.order.impl.ui.orderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.feature.home.api.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val viewModel: OrderViewModel by viewModels()

    @Inject
    lateinit var homeNavigator: HomeNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                OrderScreen(
                    viewModel = viewModel,
                    onNavigate = { destination ->
                        val intent = homeNavigator.intentFor(
                            context = requireContext(),
                            destination = destination,
                        )
                        startActivity(intent)
                    },
                )
            }
        }
    }
}
