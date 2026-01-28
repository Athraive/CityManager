package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.*
import de.geier.citymanager.ui.AccessContext

class CityViewModelFactory(
    private val context: Context,
    private val accessContext: AccessContext
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return CityViewModel(
                accessContext = accessContext,

                poiCategoryRepository =
                    PoiCategoryRepository(database.poiCategoryDao()),

                poiRepository =
                    PointOfInterestRepository(database.pointOfInterestDao()),

                factionRepository =
                    FactionRepositoryImpl(database.factionDao()),

                cityDistrictRepository =
                    CityDistrictRepositoryImpl(database.cityDistrictDao()),

                cityLoreRepository =
                    CityLoreRepository(database.cityLoreDao())
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
