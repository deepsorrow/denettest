package ru.kropotov.denet.test.compose.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BottomSheetSelect(
    items: List<T>,
    onDismiss: () -> Unit,
    title: @Composable () -> Unit = {},
    itemContent: @Composable (T) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        containerColor = Color(0xFF1A1D25),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        title()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetNodeSelectPreview() {
    BottomSheetSelect(
        items = listOf(
            "0x21c5983b5ee94b6cf7b3a041cae7d830566ac443",
            "0x21c5983b5ee94b6cf7b3a041cae7d830566ac443"
        ),
        onDismiss = {},
        itemContent = {}
    )
}