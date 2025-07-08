package ru.kropotov.denet.test.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kropotov.denet.test.data.AppDatabase
import ru.kropotov.denet.test.data.launch.LaunchRepository
import ru.kropotov.denet.test.data.launch.LaunchRepositoryImpl
import ru.kropotov.denet.test.data.node.NodeDao
import ru.kropotov.denet.test.data.node.NodeRepository
import ru.kropotov.denet.test.data.node.NodeRepositoryImpl
import ru.kropotov.denet.test.data.savedState.SavedStateDataStore
import ru.kropotov.denet.test.data.savedState.SavedStateDataStoreImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): NodeDao {
        return appDatabase.nodeDao()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface DatabaseBinds {

        @Binds
        fun bindsSavedStateDataStore(impl: SavedStateDataStoreImpl): SavedStateDataStore

        @Binds
        fun bindsLaunchRepository(impl: LaunchRepositoryImpl): LaunchRepository

        @Binds
        fun bindsNodeRepository(impl: NodeRepositoryImpl): NodeRepository
    }
}