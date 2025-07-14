package com.example.feature.product.impl.data.repository

import android.content.Context
import com.example.core.domain.DomainResult
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.impl.data.ApiProduct
import com.example.feature.product.impl.data.ApiProductMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.InputStream
import javax.inject.Inject
import kotlin.collections.emptyList

class ProductRepositoryMockImpl @Inject constructor(
    private val context: Context,
    private val mapper: ApiProductMapper,
) : ProductRepository {

    override suspend fun getProducts(): DomainResult<List<Product>> {
        return try {
            val json = readJsonFromAssets("product_mock.json")
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, ApiProduct::class.java)
            val adapter = moshi.adapter<List<ApiProduct>>(listType)
            val apiProducts = adapter.fromJson(json)
            val products = apiProducts?.map { mapper.map(it) } ?: emptyList()
            DomainResult.Success(products)
        } catch (e: Exception) {
            DomainResult.Error("Failed to load mock products: ${e.message}")
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
