package de.geier.citymanager.ui

data class AccessContext(
    val role: Role,
    val cityId: Long
) {

    fun canEdit(): Boolean =
        role == Role.GAME_MASTER

    fun canViewSlNotes(): Boolean =
        role == Role.GAME_MASTER

    fun canWritePlayerNotes(): Boolean =
        role == Role.PLAYER || role == Role.GAME_MASTER
}
