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

            val cityRepository = CityRepositoryImpl(
                cityDao = database.cityDao(),
                personDao = database.personDao(),
                poiDao = database.pointOfInterestDao(),
                factionDao = database.factionDao(),
                poiCategoryDao = database.poiCategoryDao(),
                districtDao = database.cityDistrictDao(),
                loreDao = database.cityLoreDao()
            )

            return CityViewModel(
                accessContext = accessContext,
                cityRepository = cityRepository,

                poiCategoryRepository =
                    PoiCategoryRepository(
                        accessContext.cityId,
                        database.poiCategoryDao()
                    ),

                poiRepository =
                    PointOfInterestRepository(
                        accessContext.cityId,
                        database.pointOfInterestDao()
                    ),

                factionRepository =
                    FactionRepositoryImpl(
                        accessContext.cityId,
                        database.factionDao()
                    ),

                cityDistrictRepository =
                    CityDistrictRepositoryImpl(database.cityDistrictDao()),

                cityLoreRepository =
                    CityLoreRepository(database.cityLoreDao()),

                personRepository =
                    PersonRepository(
                        accessContext.cityId,
                        database.personDao()
                    )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
