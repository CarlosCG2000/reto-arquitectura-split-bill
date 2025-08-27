package io.devexpert.splitbill.domain.useCases

import io.devexpert.splitbill.data.scan.ScanCounterRepository

class DecrementScanCounterUseCase(private val scanCounterRepository: ScanCounterRepository) {
    suspend operator fun invoke() {
        scanCounterRepository.decrementScan()
    }
}