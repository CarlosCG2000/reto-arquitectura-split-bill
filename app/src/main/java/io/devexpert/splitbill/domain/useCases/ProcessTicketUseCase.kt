package io.devexpert.splitbill.domain.useCases

import io.devexpert.splitbill.data.TicketData
import io.devexpert.splitbill.data.ticket.TicketRepository

class ProcessTicketUseCase(private val ticketRepository: TicketRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): TicketData = ticketRepository.processTicket(imageBytes)
}