package ru.kropotov.denet.test.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.jcajce.provider.digest.Keccak
import ru.kropotov.denet.test.data.AppDatabase
import ru.kropotov.denet.test.data.node.Node
import kotlin.random.Random

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val randomData = generateTreeAsList()
            database.nodeDao().upsertAll(randomData)

            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    private fun generateTreeAsList(
        maxChildren: Int = 3,
        maxDepth: Int = 5
    ): List<Node> {
        val allNodes = mutableListOf<Node>()

        fun generateSubtree(
            currentDepth: Int,
            parentAddress: String?,
            indexInParent: Int
        ): String {
            val input = "${parentAddress ?: "root"}:$currentDepth:$indexInParent"
            val address = "0x" + keccak256(input).takeLast(40).lowercase()

            if (currentDepth >= maxDepth) {
                allNodes.add(Node(address, emptyList(), parentAddress))
                return address
            }

            val childCount = Random.nextInt(1, maxChildren + 1)
            val childAddresses = mutableListOf<String>()

            repeat(childCount) { i ->
                val childAddress = generateSubtree(currentDepth + 1, address, i)
                childAddresses.add(childAddress)
            }

            allNodes.add(Node(address, childAddresses, parentAddress))
            return address
        }

        generateSubtree(currentDepth = 0, parentAddress = null, indexInParent = 0)

        return allNodes
    }

    private fun keccak256(input: String): String {
        val digest = Keccak.Digest256()
        val hash = digest.digest(input.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private companion object {
         const val TAG = "SeedDatabaseWorker"
    }
}