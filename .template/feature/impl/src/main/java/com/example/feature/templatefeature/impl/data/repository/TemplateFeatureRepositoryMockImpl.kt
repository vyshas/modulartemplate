package com.example.feature.templatefeature.impl.data.repository

import android.content.Context
import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.impl.data.ApiTemplateFeature
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.InputStream
import javax.inject.Inject

class TemplateFeatureRepositoryMockImpl @Inject constructor(
    private val context: Context,
    private val mapper: ApiTemplateFeatureMapper,
) : TemplateFeatureRepository {

    override suspend fun getTemplateFeatures(): DomainResult<List<TemplateFeature>> {
        return try {
            val json = readJsonFromAssets("templatefeature_mock.json")
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType =
                Types.newParameterizedType(List::class.java, ApiTemplateFeature::class.java)
            val adapter = moshi.adapter<List<ApiTemplateFeature>>(listType)
            val apiTemplateFeatures = adapter.fromJson(json)
            val templatefeatures = apiTemplateFeatures?.map { mapper.map(it) } ?: emptyList()
            DomainResult.Success(templatefeatures)
        } catch (e: Exception) {
            DomainResult.Error("Failed to load mock templatefeatures: ${e.message}")
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
