package de.geier.citymanager.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PoiCategoryRepository

class PoiCategoryViewModelFactory(
    private val context: Context,
    private val accessContext: AccessContext
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiCategoryViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return PoiCategoryViewModel(
                repository = PoiCategoryRepository(
                    accessContext.cityId,
                    database.poiCategoryDao()
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
