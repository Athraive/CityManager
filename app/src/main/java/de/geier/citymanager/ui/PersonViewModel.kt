@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PersonRepository
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.Person
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonViewModel(
    private val accessContext: AccessContext,
    private val personRepository: PersonRepository,
    private val personPoiRepository: PersonPoiRepository,
    private val personFactionRepository: PersonFactionRepository
) : ViewModel() {

    /* ---------------- Personen ---------------- */

    val persons: StateFlow<List<Person>> =
        personRepository.getPersons(accessContext)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    /* ---------------- Auswahl ---------------- */

    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> = _selectedPerson.asStateFlow()

    fun selectPerson(person: Person) {
        _selectedPerson.value = person
    }

    fun clearSelection() {
        _selectedPerson.value = null
    }

    /* ---------------- Persistenz ---------------- */

    fun save(person: Person) {
        viewModelScope.launch {
            personRepository.save(person, accessContext)
            if (_selectedPerson.value?.id == person.id) {
                _selectedPerson.value = person
            }
        }
    }

    fun delete(person: Person) {
        viewModelScope.launch {
            personRepository.delete(person, accessContext)
            clearSelection()
        }
    }

    /* ---------------- POI-Zuweisungen ---------------- */

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
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

    fun togglePoiAssignment(poiId: String) {
        val person = selectedPerson.value ?: return

        // 🔒 Spieler dürfen keine Beziehungen ändern
        if (!accessContext.canEdit()) return

        val current = poiIdsForSelectedPerson.value

        viewModelScope.launch {
            if (current.contains(poiId)) {
                personPoiRepository.removePoiFromPerson(person.id, poiId)
            } else {
                personPoiRepository.addPoiToPerson(person.id, poiId)
            }
        }
    }

    /* ---------------- Fraktions-Zuweisungen ---------------- */

    val factionIdsForSelectedPerson: StateFlow<Set<String>> =
        selectedPerson
            .flatMapLatest { person ->
                if (person == null) {
                    flowOf(emptyList())
                } else {
                    personFactionRepository.getFactionIdsForPerson(person.id)
                }
            }
            .map { it.toSet() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

    fun toggleFactionAssignment(factionId: String) {
        val person = selectedPerson.value ?: return

        // 🔒 Spieler dürfen keine Beziehungen ändern
        if (!accessContext.canEdit()) return

        val current = factionIdsForSelectedPerson.value

        viewModelScope.launch {
            if (current.contains(factionId)) {
                personFactionRepository.removeFactionFromPerson(person.id, factionId)
            } else {
                personFactionRepository.addFactionToPerson(person.id, factionId)
            }
        }
    }
}
