package ru.kropotov.denet.test.data.node

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey val address: String,
    val children: List<String>,
    val parent: String?
) : Parcelable