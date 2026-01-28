package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.FactionRepositoryImpl

class FactionViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactionViewModel::class.java)) {

            val database = DatabaseProvider.getDatabase(context)

            return FactionViewModel(
                repository = FactionRepositoryImpl(
                    database.factionDao()
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
