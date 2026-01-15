package de.geier.citymanager.data

import de.geier.citymanager.data.entity.CityLoreEntity
import de.geier.citymanager.data.repository.CityLoreRepository

class CityLoreSeeder(
    private val repository: CityLoreRepository
) {
    suspend fun seedIfEmpty(cityId: String) {
        repository.save(
            CityLoreEntity(
                cityId = cityId,
                title = "Geschichte der Stadt",
                text = """
                    Diese Stadt wurde vor Generationen gegründet.
                    
                    Händler, Abenteurer und Machtgruppen haben sie geprägt.
                    Ihre Geschichte ist reich an Intrigen, Umbrüchen und Legenden.
                """.trimIndent()
            )
        )
    }
}
