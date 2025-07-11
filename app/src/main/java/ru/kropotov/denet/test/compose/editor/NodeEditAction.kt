package ru.kropotov.denet.test.compose.editor

import androidx.annotation.StringRes
import ru.kropotov.denet.test.R

enum class NodeEditAction(
    @StringRes val actionRes: Int
) {

    ADD_CHILD(actionRes = R.string.action_add_child),
    DELETE(actionRes = R.string.action_delete)
}