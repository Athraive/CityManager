package de.geier.citymanager.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Globaler Rollen-State für Test- und Entwicklungszwecke.
 *
 * ❗ Absichtlich:
 * - kein Room
 * - kein ViewModel
 * - kein DI
 */
object RoleState {

    private val _role = MutableStateFlow(Role.GAMEMASTER)
    val role: StateFlow<Role> = _role.asStateFlow()

    fun toggle() {
        _role.value =
            if (_role.value == Role.GAMEMASTER) Role.PLAYER
            else Role.GAMEMASTER
    }

    fun set(role: Role) {
        _role.value = role
    }
}
