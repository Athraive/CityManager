package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.ui.AccessContext

class PoiViewModelFactory(
    private val context: Context,
    private val accessContext: AccessContext
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return PoiViewModel(
                accessContext = accessContext,
                poiRepository =
                    PointOfInterestRepository(database.pointOfInterestDao()),
                poiFactionRepository =
                    PoiFactionRepository(database.poiFactionDao()),
                personPoiRepository =
                    PersonPoiRepository(database.personPoiDao())
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
