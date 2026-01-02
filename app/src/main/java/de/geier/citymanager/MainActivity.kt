package de.geier.citymanager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.geier.citymanager.ui.theme.CityManagerTheme
import de.geier.citymanager.ui.StartScreen
import de.geier.citymanager.ui.RoleSelectScreen
import de.geier.citymanager.ui.GameMasterCodeScreen
import de.geier.citymanager.ui.PointOfInterestScreen
import de.geier.citymanager.ui.PoiType
import de.geier.citymanager.ui.GameMasterDashboardScreen

enum class Screen {
    START,
    ROLE_SELECT,
    PLAYER_DASHBOARD,
    GM_CODE,
    GM_DASHBOARD,
    POIS_LOCATIONS,
    POIS_SHOPS
}




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CityManagerTheme {

                var currentScreen by remember { mutableStateOf(Screen.START) }

                when (currentScreen) {

                    Screen.START -> {
                        StartScreen(
                            cityName = "Eldoria",
                            onStartClicked = {
                                currentScreen = Screen.ROLE_SELECT
                            }
                        )
                    }

                    Screen.ROLE_SELECT -> {
                        RoleSelectScreen(
                            onPlayerClick = {
                                currentScreen = Screen.PLAYER_DASHBOARD
                            },
                            onGameMasterClick = {
                                currentScreen = Screen.GM_CODE
                            }
                        )
                    }

                    Screen.PLAYER_DASHBOARD -> {
                        Text("Spieler-Dashboard (kommt später)")
                    }

                    Screen.GM_CODE -> {
                        GameMasterCodeScreen(
                            onSuccess = {
                                currentScreen = Screen.GM_DASHBOARD
                            },
                            onBack = {
                                currentScreen = Screen.ROLE_SELECT
                            }
                        )
                    }

                    Screen.GM_DASHBOARD -> {
                        GameMasterDashboardScreen(
                            onBack = { currentScreen = Screen.ROLE_SELECT },
                            onCityDescription = { /* später */ },
                            onLocations = { currentScreen = Screen.POIS_LOCATIONS },
                            onShops = { currentScreen = Screen.POIS_SHOPS },
                            onPeople = { /* später */ },
                            onGroups = { /* später */ },
                            onNotes = { /* später */ },
                            onVisibility = { /* später */ }
                        )
                    }

                    Screen.POIS_LOCATIONS -> {
                        PointOfInterestScreen(
                            poiType = PoiType.LOCATION,
                            onBack = { currentScreen = Screen.GM_DASHBOARD }
                        )
                    }

                    Screen.POIS_SHOPS -> {
                        PointOfInterestScreen(
                            poiType = PoiType.SHOP,
                            onBack = { currentScreen = Screen.GM_DASHBOARD }
                        )
                    }

                    else -> {
                        Text("Unbekannter Screen")
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityManagerTheme {
        Greeting("Android")
    }
}