package de.geier.citymanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PersonRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonViewModel(
    private val personRepository: PersonRepository,
    private val personPoiRepository: PersonPoiRepository
) : ViewModel() {

    /* ---------------- Personen ---------------- */

    val persons = personRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> = _selectedPerson

    fun selectPerson(person: Person) {
        _selectedPerson.value = person
    }

    fun clearSelection() {
        _selectedPerson.value = null
    }

    fun save(person: Person) {
        viewModelScope.launch {
            personRepository.save(person)

            // 🔹 State aktuell halten
            if (_selectedPerson.value?.id == person.id) {
                _selectedPerson.value = person
            }
        }
    }

    fun delete(person: Person) {
        viewModelScope.launch {
            personRepository.delete(person)
            clearSelection()
        }
    }

    /* ---------------- POI-Zuordnungen ---------------- */

    val poiIdsForSelectedPerson: StateFlow<Set<String>> =
        selectedPerson
            .flatMapLatest { person ->
                if (person == null) {
                    flowOf(emptyList())
                } else {
                    personPoiRepository.getPoiIdsForPerson(person.id)
                }
            }
            .map { it.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    fun togglePoiAssignment(poiId: String) {
        val person = selectedPerson.value ?: return
        val current = poiIdsForSelectedPerson.value

        viewModelScope.launch {
            if (current.contains(poiId)) {
                personPoiRepository.removePoiFromPerson(person.id, poiId)
            } else {
                personPoiRepository.addPoiToPerson(person.id, poiId)
            }
        }
    }
}
