package de.geier.citymanager.ui

/**
 * Kontext für Rollen- und Stadtzugriff.
 *
 * cityId ist eine UUID (String),
 * da Städte eigenständig gekapselte Einheiten sind
 * und später mit Firestore synchronisiert werden.
 */
data class AccessContext(
    val role: Role,
    val cityId: String
) {

    fun canEdit(): Boolean =
        role == Role.GAME_MASTER

    fun canViewSlNotes(): Boolean =
        role == Role.GAME_MASTER

    fun canWritePlayerNotes(): Boolean =
        role == Role.PLAYER || role == Role.GAME_MASTER
}
