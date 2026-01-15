package de.geier.citymanager.data

import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CityDistrictSeeder {

    fun seedIfEmpty(
        scope: CoroutineScope,
        cityViewModel: CityViewModel,
        cityId: String
    ) {
        scope.launch {
            val existing = cityViewModel
                .districtsForCity(cityId)
                .value

            if (existing.isNotEmpty()) return@launch

            val districts = listOf(
                CityDistrictEntity(
                    id = "harbor",
                    cityId = cityId,
                    name = "Hafenviertel",
                    description =
                        "Das Hafenviertel ist das geschäftige Tor der Stadt zur Welt. "
                                + "Schiffe aus fernen Ländern legen hier an, während Händler, "
                                + "Seeleute und zwielichtige Gestalten die Kais bevölkern.",
                    orderIndex = 0,
                    mapKey = "harbor"
                ),
                CityDistrictEntity(
                    id = "upper_city",
                    cityId = cityId,
                    name = "Oberstadt",
                    description =
                        "Die Oberstadt thront über dem Rest der Stadt. Breite Straßen, "
                                + "repräsentative Bauten und der Sitz des Rates prägen diesen "
                                + "Teil der Metropole.",
                    orderIndex = 1,
                    mapKey = "upper_city"
                ),
                CityDistrictEntity(
                    id = "lower_quarter",
                    cityId = cityId,
                    name = "Unterstadt",
                    description =
                        "Enge Gassen, dicht gedrängte Häuser und ein raues Leben bestimmen "
                                + "die Unterstadt. Hier schlägt das wahre Herz der Bevölkerung.",
                    orderIndex = 2,
                    mapKey = "lower_quarter"
                )
            )

            districts.forEach { cityViewModel.saveDistrict(it) }
        }
    }
}
