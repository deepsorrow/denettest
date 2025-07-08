package ru.kropotov.denet.test.data.node

import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    fun getNodes(): Flow<List<Node>>

    fun getRootNode(): Flow<Node?>

    fun getNode(nodeAddress: String): Flow<Node?>

    suspend fun saveLastNode(nodeAddress: String)
}