package de.geier.citymanager.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    suspend fun ensureAuthenticated(): String {

        val currentUser = auth.currentUser

        if (currentUser != null) {
            return currentUser.uid
        }

        val result = auth.signInAnonymously().await()

        return result.user?.uid
            ?: throw IllegalStateException(
                "Firebase Anonymous Authentication failed"
            )
    }
}