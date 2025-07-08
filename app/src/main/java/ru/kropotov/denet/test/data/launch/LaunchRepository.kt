package ru.kropotov.denet.test.data.launch

import kotlinx.coroutines.flow.Flow
import ru.kropotov.denet.test.data.savedState.SavedState
import javax.inject.Singleton

@Singleton
interface LaunchRepository {

    fun getSavedState(): Flow<SavedState?>
}