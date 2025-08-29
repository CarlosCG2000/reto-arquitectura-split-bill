package io.devexpert.splitbill.domain.useCases

import io.devexpert.splitbill.data.Ticket
import io.devexpert.splitbill.data.ticket.TicketRepository

class ProcessTicketUseCase(private val ticketRepository: TicketRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): Ticket = ticketRepository.processTicket(imageBytes)
}