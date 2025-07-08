package ru.kropotov.denet.test.data.savedState

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface SavedStateDataStore {

    fun getSavedState(context: Context): Flow<SavedState?>
    suspend fun updateLastNodeAddress(context: Context, nodeAddress: String)
}