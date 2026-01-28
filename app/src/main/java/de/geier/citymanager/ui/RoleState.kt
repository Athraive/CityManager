package de.geier.citymanager.ui

data class RoleState(
    val role: Role? = null
) {

    val isGameMaster: Boolean
        get() = role == Role.GAME_MASTER

    val isPlayer: Boolean
        get() = role == Role.PLAYER
}
