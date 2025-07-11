package ru.kropotov.denet.test.data.node

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface NodeDao {

    @Query("SELECT * FROM nodes")
    fun getNodes(): Flow<List<Node>>

    @Query("SELECT * FROM nodes WHERE parent is null LIMIT 1")
    fun getRootNode(): Flow<Node?>

    @Query("SELECT * FROM nodes WHERE address = :nodeAddress")
    fun getNodeByAddress(nodeAddress: String): Flow<Node?>

    @Query("SELECT * FROM nodes WHERE parent = :nodeAddress LIMIT 1")
    fun getNodeParent(nodeAddress: String): Flow<Node?>

    @Query("SELECT * FROM nodes WHERE parent = :nodeAddress")
    fun getNodeChildren(nodeAddress: String): Flow<List<Node>>

    @Upsert
    suspend fun upsertAll(nodes: List<Node>)

    @Query("DELETE FROM nodes WHERE address IN (:addresses)")
    suspend fun deleteNodes(addresses: List<String>)

    suspend fun deleteNode(nodeAddress: String): List<String> {
        // remove from parent's children
        getNodeByAddress(nodeAddress).first()?.parent?.let { parentAddress ->
            val parent = getNodeByAddress(parentAddress).first() ?: return@let
            val updatedParent = parent.copy(
                children = parent.children.filter { child -> child != nodeAddress }
            )
            upsertAll(listOf(updatedParent))
        }

        // delete node and children
        val allNodes = getNodes().first()
        val toDelete = collectNodesToDelete(nodeAddress, allNodes)
        deleteNodes(toDelete)

        return toDelete
    }

    private fun collectNodesToDelete(
        root: String,
        allNodes: List<Node>,
        collected: MutableSet<String> = mutableSetOf()
    ): List<String> {
        if (!collected.add(root)) return collected.toList()

        val children = allNodes.filter { it.parent == root }
        for (child in children) {
            collectNodesToDelete(child.address, allNodes, collected)
        }

        return collected.toList()
    }
}