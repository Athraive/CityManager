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
    context: Context,
    private val accessContext: AccessContext
) : ViewModelProvider.Factory {

    private val database = DatabaseProvider.getDatabase(context)

    private val personRepository =
        PersonRepository(database.personDao())

    private val personPoiRepository =
        PersonPoiRepository(database.personPoiDao())

    private val personFactionRepository =
        PersonFactionRepository(database.personFactionDao())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            return PersonViewModel(
                accessContext = accessContext,
                personRepository = personRepository,
                personPoiRepository = personPoiRepository,
                personFactionRepository = personFactionRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
