package ru.kropotov.denet.test.data.launch

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import ru.kropotov.denet.test.data.savedState.SavedState
import ru.kropotov.denet.test.data.savedState.SavedStateDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateDataStore: SavedStateDataStore
): LaunchRepository {

    override fun getSavedState(): Flow<SavedState?> =
        savedStateDataStore.getSavedState(context)
}