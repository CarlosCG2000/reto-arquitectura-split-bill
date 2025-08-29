package io.devexpert.splitbill.presentation.receipt

import io.devexpert.splitbill.data.Ticket
import io.devexpert.splitbill.data.TicketItem

data class ReceiptUiState(
    val ticketData: Ticket? = null,
    val selectedQuantities: Map<TicketItem, Int> = emptyMap(),
    val paidQuantities: Map<TicketItem, Int> = emptyMap(),
    val availableItems: List<Pair<TicketItem, Int>> = emptyList(),
    val paidItems: List<Pair<TicketItem, Int>> = emptyList(),
    val selectedTotal: Double = 0.0
)