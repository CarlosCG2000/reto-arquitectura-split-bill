package io.devexpert.splitbill.presentation.receipt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.devexpert.splitbill.R
import io.devexpert.splitbill.data.TicketItem
import java.util.Locale

@Composable
fun SelectableTicketItemCard(
    item: TicketItem,
    availableQuantity: Int,
    selectedQuantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cantidad original en círculo
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${availableQuantity}x",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Información del item
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "€${String.format(Locale.getDefault(), "%.2f", item.price)} ${stringResource(
                        R.string.each)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Controles de selección
            if (availableQuantity == 1) {
                // Checkbox para items de cantidad 1
                Checkbox(
                    checked = selectedQuantity > 0,
                    onCheckedChange = { checked ->
                        onQuantityChange(if (checked) 1 else 0)
                    }
                )
            } else {
                // Contador con botones +/- para items con más cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (selectedQuantity > 0) {
                                onQuantityChange(selectedQuantity - 1)
                            }
                        },
                        enabled = selectedQuantity > 0
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = stringResource(R.string.reduce_quantity)
                        )
                    }

                    Text(
                        text = selectedQuantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = {
                            if (selectedQuantity < availableQuantity) {
                                onQuantityChange(selectedQuantity + 1)
                            }
                        },
                        enabled = selectedQuantity < availableQuantity
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.increase_quantity)
                        )
                    }
                }
            }
        }
    }
}