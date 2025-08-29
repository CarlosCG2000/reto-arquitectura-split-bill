package io.devexpert.splitbill.data.ticket

import io.devexpert.splitbill.data.Ticket


class TicketRepository(private val dataSource: TickectDataSource) {
    private var _ticketData: Ticket? = null

    suspend fun processTicket(image: ByteArray): Ticket {
        val result = dataSource.processTicket(image)
        _ticketData = result
        return result
    }

    fun getTicketData(): Ticket? = _ticketData

}