package de.geier.citymanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.ui.CityScreen
import de.geier.citymanager.ui.Role
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.CityViewModelFactory

@Composable
fun CityScreenGate(
    role: Role?,
    cityId: Long?
) {
    // 🔒 Kein gültiger Kontext → nichts anzeigen (oder später Redirect)
    if (role == null || cityId == null) return

    val context = LocalContext.current
    val cityViewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(context)
    )

    val accessContext = AccessContext(
        role = role,
        cityId = cityId
    )

    CityScreen(
        cityViewModel = cityViewModel,
        accessContext = accessContext
    )
}
