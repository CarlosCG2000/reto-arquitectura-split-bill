package io.devexpert.splitbill.data.ticket

import io.devexpert.splitbill.data.TicketData


class TicketRepository(private val dataSource: TickectDataSource) {
    private var _ticketData: TicketData? = null

    suspend fun processTicket(image: ByteArray): TicketData {
        val result = dataSource.processTicket(image)
        _ticketData = result
        return result
    }

    fun getTicketData(): TicketData? = _ticketData

}