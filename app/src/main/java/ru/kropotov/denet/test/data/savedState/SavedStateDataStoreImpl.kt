package ru.kropotov.denet.test.data.savedState

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "savedState")

class SavedStateDataStoreImpl @Inject constructor() : SavedStateDataStore {

    override fun getSavedState(context: Context): Flow<SavedState?> =
        context.dataStore.data.map { preferences ->
            SavedState(
                lastNodeAddress = preferences[NODE_ADDRESS_KEY]
            )
        }

    override suspend fun updateLastNodeAddress(
        context: Context,
        nodeAddress: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[NODE_ADDRESS_KEY] = nodeAddress
        }
    }

    private companion object {
        val NODE_ADDRESS_KEY = stringPreferencesKey("node_address")
    }
}