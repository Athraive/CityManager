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

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiCategoryViewModel::class.java)) {
            val db = DatabaseProvider.getDatabase(context)
            val repository = PoiCategoryRepository(db.poiCategoryDao())

            @Suppress("UNCHECKED_CAST")
            return PoiCategoryViewModel(
                accessContext = accessContext,
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
