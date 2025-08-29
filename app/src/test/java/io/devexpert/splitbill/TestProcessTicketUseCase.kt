package io.devexpert.splitbill

import io.devexpert.splitbill.data.ticket.TicketRepository
import io.devexpert.splitbill.domain.useCases.ProcessTicketUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestProcessTicketUseCase {

    private lateinit var testTicketDataSource: TestTicketDataSource // Fuente de datos de prueba
    private lateinit var ticketRepository: TicketRepository // Repositorio a probar
    private lateinit var processTicketUseCase: ProcessTicketUseCase // Caso de uso a probar

    @Before
    fun setUp() {
        testTicketDataSource = TestTicketDataSource()
        ticketRepository = TicketRepository(testTicketDataSource)
        processTicketUseCase = ProcessTicketUseCase(ticketRepository)
    }

    @Test
    fun `processed ticket contains expected number of items`() = runTest {
        // Given
        val imageBytes = byteArrayOf(1, 2, 3)

        // When
        val result = processTicketUseCase(imageBytes)

        // Then
        assertEquals(21, result.items.size)
    }

    @Test
    fun `processed ticket contains correct item details`() = runTest {
        // Given
        val imageBytes = byteArrayOf(9, 9, 9)

        // When
        val result = processTicketUseCase(imageBytes)

        // Then
        val pan = result.items.first { it.name == "PAN" }
        assertEquals(7, pan.quantity)
        assertEquals(1.90, pan.price, 0.01)
    }

    @Test
    fun `calculated total matches sum of items`() = runTest {
        // Given
        val imageBytes = byteArrayOf(7, 7, 7)

        // When
        val result = processTicketUseCase(imageBytes)

        // Then
        val expectedTotal = result.items.sumOf { it.quantity * it.price }
        assertEquals(expectedTotal, result.total, 0.01)
    }

    @Test
    fun `invoke processes ticket with mock data successfully`() = runTest {
        // Given
        val imageBytes = byteArrayOf(1, 2, 3, 4, 5)

        // When
        val result = processTicketUseCase(imageBytes)

        // Then
        assertEquals(272.20, result.total, 0.01)
    }


}