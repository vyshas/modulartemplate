package com.example.feature.product.impl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.feature.product.impl.R
import com.example.feature.product.impl.ui.productlist.ProductListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, ProductListFragment())
            }
        }
    }
}
