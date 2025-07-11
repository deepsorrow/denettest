package ru.kropotov.denet.test.data.savedState

import kotlinx.coroutines.flow.Flow

interface SavedStateDataStore {

    fun getSavedState(): Flow<SavedState?>
    suspend fun updateLastNodeAddress(nodeAddress: String?)
}