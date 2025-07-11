package ru.kropotov.denet.test.compose.editor

data class NodeUiModel(
    val address: String,
    val parent: String?,
    val children: List<String>,
    val isExpanded: Boolean = false
) {

    companion object {
        fun NodeUiModel.toSavable(): List<Any?> = listOf(address, parent, children, isExpanded)

        fun List<Any?>.toNodeUiModel(): NodeUiModel {
            val address = this[0] as String
            val parent = this[1] as String?
            val children = this[2] as List<String>
            val isExpanded = this[3] as Boolean
            return NodeUiModel(address, parent, children, isExpanded)
        }
    }
}