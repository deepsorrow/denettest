package ru.kropotov.denet.test.compose.node

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.R
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.ui.theme.LocalCustomColors
import ru.kropotov.denet.test.viewmodel.NodeViewModel

@Composable
fun NodeScreen(
    viewModel: NodeViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit = {},
) {
    val node = viewModel.node.collectAsStateWithLifecycle().value ?: return
    NodeScreen(
        node = node,
        navigateTo = navigateTo
    )
}

@Composable
fun NodeScreen(
    node: Node,
    navigateTo: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 56.dp)
            .fillMaxSize()
    ) {
        val (textTitle, btnSettings, textNodeType, textNodeAddress, abstractShape, buttons) = createRefs()

        Text(
            modifier = Modifier.constrainAs(textTitle) {
                top.linkTo(anchor = btnSettings.top)
                start.linkTo(anchor = parent.start)
                bottom.linkTo(anchor = btnSettings.bottom)
            },
            text = "Node Screen",
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 26.sp
        )
        Button(
            modifier = Modifier
                .constrainAs(btnSettings) {
                    top.linkTo(anchor = parent.top)
                    end.linkTo(anchor = parent.end)
                }
                .size(52.dp),
            onClick = {},
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(CornerSize(28f)),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings"
            )
        }

        AbstractNodeShape(
            modifier = Modifier.constrainAs(abstractShape) {
                top.linkTo(anchor = parent.top, margin = (-140).dp)
                bottom.linkTo(anchor = parent.bottom)
                start.linkTo(anchor = parent.start, margin = 16.dp)
                end.linkTo(anchor = parent.end, margin = 16.dp)
            },
            seed = node.address,
            size = 180.dp
        )

        Text(
            modifier = Modifier.constrainAs(textNodeType) {
                top.linkTo(anchor = abstractShape.bottom, margin = 16.dp)
                start.linkTo(anchor = parent.start, margin = 16.dp)
                end.linkTo(anchor = parent.end, margin = 16.dp)
            },
            text = if (node.parent == null) "Root Node" else "Node",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 26.sp
        )
        Text(
            modifier = Modifier
                .constrainAs(textNodeAddress) {
                    top.linkTo(anchor = textNodeType.bottom)
                    start.linkTo(anchor = textNodeType.start)
                    end.linkTo(anchor = textNodeType.end)
                },
            text = node.address,
            textAlign = TextAlign.Center,
            color = LocalCustomColors.current.accent,
            fontSize = 26.sp
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(58.dp)
                .constrainAs(buttons) {
                    bottom.linkTo(anchor = parent.bottom)
                    start.linkTo(anchor = parent.start)
                    end.linkTo(anchor = parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            if (node.parent != null) {
                Button(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(CornerSize(28f)),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    onClick = { navigateTo(node.parent) }
                ) {
                    Text(
                        text = "Back",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 20.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.weight(0.1f, true))
            if (node.children.count() > 0) {
                Button(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(CornerSize(28f)),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        navigateTo(node.children[0])
                    }
                ) {
                    Text(
                        text = "Next",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
private fun NodeScreenPreview() {
    val node = Node(
        address = "0x21c5983b5ee94b6cf7b3a041cae7d830566ac443",
        children = listOf(""),
        parent = ""
    )
    NodeScreen(node, {})
}