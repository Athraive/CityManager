package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import de.geier.citymanager.ui.Faction
import de.geier.citymanager.ui.FactionRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel für Fraktionen.
 *
 * - Stellt Fraktionen als StateFlow bereit
 * - Reicht Speicher- und Löschoperationen weiter
 * - Enthält KEINE UI-Logik
 */
class FactionViewModel(
    private val repository: FactionRepository
) : ViewModel() {

    /**
     * Öffentlicher Zugriff auf alle Fraktionen.
     *
     * UI kann dieses StateFlow beobachten.
     */
    val factions: StateFlow<List<Faction>> = repository.factions

    /**
     * Speichert eine Fraktion (neu oder Update).
     */
    fun save(faction: Faction) {
        repository.save(faction)
    }

    /**
     * Löscht eine Fraktion anhand ihrer ID.
     */
    fun delete(factionId: String) {
        repository.delete(factionId)
    }

    /**
     * Liefert eine Fraktion anhand ihrer ID oder null.
     */
    fun getById(factionId: String): Faction? {
        return repository.getById(factionId)
    }
}
