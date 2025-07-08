package ru.kropotov.denet.test.data.node

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import ru.kropotov.denet.test.data.savedState.SavedStateDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val nodeDao: NodeDao,
    private val dataStore: SavedStateDataStore
) : NodeRepository {

    override fun getNodes(): Flow<List<Node>> = nodeDao.getNodes()

    override fun getRootNode(): Flow<Node?> =
        nodeDao.getRootNode()

    override fun getNode(nodeAddress: String): Flow<Node?> =
        nodeDao.getNodeByAddress(nodeAddress)

    override suspend fun saveLastNode(nodeAddress: String) {
        dataStore.updateLastNodeAddress(context, nodeAddress)
    }
}