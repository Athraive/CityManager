package de.geier.citymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.geier.citymanager.ui.navigation.CityNavHost
import de.geier.citymanager.ui.theme.CityManagerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CityManagerTheme {
                CityNavHost()
            }
        }
    }
}
