package com.example.feature.home.impl.ui.homedetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feature.home.impl.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HomeDetailFragment().apply {
                        arguments = Bundle().apply {
                            putInt(
                                HomeDetailViewModel.KEY_ITEM_ID,
                                intent.getIntExtra(
                                    HomeDetailViewModel.KEY_ITEM_ID,
                                    -1
                                )
                            )
                        }
                    }).commitNow()

        }
    }

    companion object {
        const val KEY_ITEM_ID = "item_id"
    }
}