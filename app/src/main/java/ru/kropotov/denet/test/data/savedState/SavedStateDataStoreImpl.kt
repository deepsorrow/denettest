package ru.kropotov.denet.test.data.savedState

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "savedState")

class SavedStateDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SavedStateDataStore {

    override fun getSavedState(): Flow<SavedState?> =
        context.dataStore.data.map { preferences ->
            SavedState(
                lastNodeAddress = preferences[NODE_ADDRESS_KEY]
            )
        }

    override suspend fun updateLastNodeAddress(nodeAddress: String?) {
        context.dataStore.edit { preferences ->
            if (nodeAddress == null) {
                preferences.remove(NODE_ADDRESS_KEY)
                return@edit
            }
            preferences[NODE_ADDRESS_KEY] = nodeAddress
        }
    }

    private companion object {
        val NODE_ADDRESS_KEY = stringPreferencesKey("node_address")
    }
}