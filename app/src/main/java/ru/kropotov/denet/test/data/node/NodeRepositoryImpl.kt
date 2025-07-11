package ru.kropotov.denet.test.data.node

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kropotov.denet.test.data.savedState.SavedStateDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodeRepositoryImpl @Inject constructor(
    private val nodeDao: NodeDao,
    private val dataStore: SavedStateDataStore
) : NodeRepository {

    override fun getNodes(): Flow<List<Node>> = nodeDao.getNodes()

    override fun getRootNode(): Flow<Node?> = nodeDao.getRootNode()

    override fun getNode(nodeAddress: String): Flow<Node?> =
        nodeDao.getNodeByAddress(nodeAddress)

    override fun getLastNodeAddress(): Flow<String?> =
        dataStore.getSavedState().map { it?.lastNodeAddress }

    override suspend fun saveLastNode(nodeAddress: String?) =
        dataStore.updateLastNodeAddress(nodeAddress)

    override suspend fun deleteNode(nodeAddress: String): List<String> =
        nodeDao.deleteNode(nodeAddress)

    override suspend fun addChildNode(parent: String, nodeAddress: String) {
        val newNode = Node(
            address = nodeAddress,
            children = listOf(),
            parent = parent
        )

        val updatedParent = getNode(parent).first()?.let { parent ->
            parent.copy(
                children = parent.children.toMutableList().apply { add(nodeAddress) }
            )
        }
        nodeDao.upsertAll(listOfNotNull(newNode, updatedParent))
    }
}