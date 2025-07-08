package ru.kropotov.denet.test.data.node

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

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
    fun getNodeChildren(nodeAddress: String): Flow<List<Node>?>

    @Upsert
    suspend fun upsertAll(nodes: List<Node>)
}