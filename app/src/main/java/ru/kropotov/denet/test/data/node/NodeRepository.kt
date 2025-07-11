package ru.kropotov.denet.test.data.node

import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    fun getNodes(): Flow<List<Node>>

    fun getRootNode(): Flow<Node?>

    fun getNode(nodeAddress: String): Flow<Node?>

    fun getLastNodeAddress(): Flow<String?>

    suspend fun saveLastNode(nodeAddress: String?)

    /**
     * Deletes node, it's children and removes from parent's children.
     *
     * @return removed nodes
     */
    suspend fun deleteNode(nodeAddress: String): List<String>

    suspend fun addChildNode(parent: String, nodeAddress: String)
}