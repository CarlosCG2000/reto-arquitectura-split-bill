package io.devexpert.splitbill.data.ticket

import io.devexpert.splitbill.data.TicketData

interface TickectDataSource {
    suspend fun processTicket(image: ByteArray): TicketData
}