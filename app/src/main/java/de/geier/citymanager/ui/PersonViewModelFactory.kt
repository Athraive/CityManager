package de.geier.citymanager.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PersonRepository

class PersonViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = DatabaseProvider.getDatabase(context)

        return PersonViewModel(
            personRepository = PersonRepository(db.personDao()),
            personPoiRepository = PersonPoiRepository(db.personPoiDao())
        ) as T
    }
}
