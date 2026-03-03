package de.geier.citymanager.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.viewmodel.CityViewModel

class MapViewModelFactory(
    private val cityViewModel: CityViewModel,
    private val personFactionRepository: PersonFactionRepository,
    private val poiFactionRepository: PoiFactionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(
            cityViewModel,
            personFactionRepository,
            poiFactionRepository
        ) as T
    }
}