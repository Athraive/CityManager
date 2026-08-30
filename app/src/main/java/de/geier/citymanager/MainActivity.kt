package de.geier.citymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import de.geier.citymanager.data.FirebaseAuthManager
import de.geier.citymanager.ui.navigation.AppNavHost
import de.geier.citymanager.ui.theme.CityManagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAuthManager = FirebaseAuthManager()

        lifecycleScope.launch {

            try {

                val uid =
                    firebaseAuthManager.ensureAuthenticated()

                println(
                    "Firebase Anonymous Auth successful: uid=$uid"
                )

                setContent {

                    CityManagerTheme {
                        AppNavHost()
                    }
                }

            } catch (e: Exception) {

                println(
                    "Firebase Anonymous Auth failed: ${e.message}"
                )

                e.printStackTrace()
            }
        }
    }
}