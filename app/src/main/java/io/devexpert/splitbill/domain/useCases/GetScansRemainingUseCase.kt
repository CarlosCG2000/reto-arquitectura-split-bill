package io.devexpert.splitbill.domain.useCases

import io.devexpert.splitbill.data.scan.ScanCounterRepository
import kotlinx.coroutines.flow.Flow

class GetScansRemainingUseCase(private val scanCounterRepository: ScanCounterRepository) {
    operator fun invoke(): Flow<Int> = scanCounterRepository.scansRemaining
}