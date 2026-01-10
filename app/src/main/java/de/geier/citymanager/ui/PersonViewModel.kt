package de.geier.citymanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonViewModel(
    private val repository: PersonRepository
) : ViewModel() {

    /**
     * Alle Personen (SL-Sicht).
     * Für Spieler kann später gezielt getVisible() verwendet werden.
     */
    val persons = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Aktuell ausgewählte Person (für Detailansicht).
     */
    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> = _selectedPerson

    /**
     * Setzt die aktuell ausgewählte Person.
     */
    fun selectPerson(person: Person) {
        _selectedPerson.value = person
    }

    /**
     * Hebt die Auswahl auf (z. B. bei Zurücknavigation).
     */
    fun clearSelection() {
        _selectedPerson.value = null
    }

    /**
     * Speichert eine Person.
     * Wird für:
     * - SL-Bearbeitung
     * - gemeinsame Notizen
     * verwendet.
     */
    fun save(person: Person) {
        viewModelScope.launch {
            repository.save(person)
        }
    }

    /**
     * Löscht eine Person (nur SL).
     */
    fun delete(person: Person) {
        viewModelScope.launch {
            repository.delete(person)
            clearSelection()
        }
    }
}
