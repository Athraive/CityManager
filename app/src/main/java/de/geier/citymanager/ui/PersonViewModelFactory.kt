package de.geier.citymanager.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PersonRepository
import de.geier.citymanager.ui.viewmodel.PersonViewModel

class PersonViewModelFactory(
    private val context: Context,
    private val accessContext: AccessContext
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return PersonViewModel(
                accessContext = accessContext,
                personRepository =
                    PersonRepository(database.personDao()),
                personPoiRepository =
                    PersonPoiRepository(database.personPoiDao()),
                personFactionRepository =
                    PersonFactionRepository(database.personFactionDao())
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
