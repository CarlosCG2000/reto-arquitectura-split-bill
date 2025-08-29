package io.devexpert.splitbill.domain.useCases

import io.devexpert.splitbill.data.Ticket
import io.devexpert.splitbill.data.ticket.TicketRepository

class GetTicketDataUseCase(private val ticketRepository: TicketRepository) {
    operator fun invoke(): Ticket? = ticketRepository.getTicketData()
}