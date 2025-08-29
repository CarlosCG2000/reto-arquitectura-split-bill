package io.devexpert.splitbill.ui.receipt

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.devexpert.splitbill.R
import io.devexpert.splitbill.data.ticket.TicketRepository
import io.devexpert.splitbill.data.TicketItem
import io.devexpert.splitbill.di.AppModule
import io.devexpert.splitbill.domain.useCases.GetTicketDataUseCase
import java.util.Locale
import kotlin.compareTo
import kotlin.text.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    viewModel: ReceiptViewModel,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ReceiptScreenContent(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onQuantityChange = viewModel::onQuantityChange,
        onMarkAsPaid = viewModel::onMarkAsPaid
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreenContent(
    uiState: ReceiptUiState,
    onBackPressed: () -> Unit,
    onQuantityChange: (TicketItem, Int) -> Unit,
    onMarkAsPaid: () -> Unit
) {
    if (uiState.ticketData == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_ticket_data),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = onBackPressed) {
                Text(stringResource(R.string.back))
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.receipt)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.availableItems) { (item, availableQty) ->
                    val selectedQty = uiState.selectedQuantities[item] ?: 0

                    SelectableTicketItemCard(
                        item = item,
                        availableQuantity = availableQty,
                        selectedQuantity = selectedQty,
                        onQuantityChange = { newQty ->
                            onQuantityChange(item, newQty)
                        }
                    )
                }

                items(uiState.paidItems) { (item, paidQty) ->
                    PaidTicketItemCard(
                        item = item,
                        paidQuantity = paidQty
                    )
                }
            }

            if (uiState.selectedTotal > 0) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.selected_total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "€${String.format(Locale.getDefault(), "%.2f", uiState.selectedTotal)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onMarkAsPaid,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.mark_as_paid),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}