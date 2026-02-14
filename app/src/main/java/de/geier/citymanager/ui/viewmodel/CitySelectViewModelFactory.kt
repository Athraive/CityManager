package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.CityRepositoryImpl

class CitySelectViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CitySelectViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return CitySelectViewModel(
                repository = CityRepositoryImpl(
                    cityDao = database.cityDao(),
                    personDao = database.personDao(),
                    poiDao = database.pointOfInterestDao(),
                    factionDao = database.factionDao(),
                    poiCategoryDao = database.poiCategoryDao(),
                    districtDao = database.cityDistrictDao(),
                    loreDao = database.cityLoreDao()
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
