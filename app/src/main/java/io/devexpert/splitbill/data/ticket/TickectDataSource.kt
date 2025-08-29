package io.devexpert.splitbill.data.ticket

import io.devexpert.splitbill.data.Ticket

interface TickectDataSource {
    suspend fun processTicket(image: ByteArray): Ticket
}