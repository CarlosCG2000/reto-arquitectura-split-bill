package io.devexpert.splitbill.data.ticket

import android.graphics.Bitmap
import io.devexpert.splitbill.domain.TicketData

interface TickectDataSource {
    suspend fun processTicket(image: ByteArray): TicketData
}