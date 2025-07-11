package ru.kropotov.denet.test.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kropotov.denet.test.R
import ru.kropotov.denet.test.ui.theme.buttonShape
import ru.kropotov.denet.test.ui.theme.titleLarge

enum class ToolbarState(
    @StringRes val title: Int = 0,
    val hasBackButton: Boolean = false,
    val hasRightButton: Boolean = false,
    val rightButtonAction: (NavController) -> Unit = {}
) {

    NodeScreenState(
        title = R.string.screen_node_title,
        hasBackButton = false,
        hasRightButton = true,
        rightButtonAction = { navController -> navController.navigate(Screen.Editor.route) }
    ),

    EditorScreenState(
        title = R.string.screen_editor_title,
        hasBackButton = true,
        hasRightButton = false
    )
}

@Composable
fun TopBar(
    state: ToolbarState?,
    navController: NavController
) {
    if (state == null) return

    val startPadding  = if (state.hasBackButton) 16.dp else 24.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = startPadding, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.hasBackButton) {
            Button(
                onClick = { navController.popBackStack() },
                contentPadding = PaddingValues(0.dp),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .size(52.dp)
                    .padding(end = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.content_description_toolbar_back_button),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (state.title != 0) {
            Text(
                text = stringResource(state.title),
                style = titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.weight(1f)
            )
        }

        if (state.hasRightButton) {
            Button(
                onClick = { state.rightButtonAction.invoke(navController) },
                contentPadding = PaddingValues(0.dp),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.size(52.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.content_description_topbar_right_btn),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}