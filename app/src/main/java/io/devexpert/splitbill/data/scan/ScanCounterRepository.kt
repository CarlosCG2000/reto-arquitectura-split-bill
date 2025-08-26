package io.devexpert.splitbill.data.scan

import io.devexpert.splitbill.data.scan.ScanCounterDataSource
import kotlinx.coroutines.flow.Flow

class ScanCounterRepository (private val scanCounterDataSource: ScanCounterDataSource) {
    val scansRemaining: Flow<Int> = scanCounterDataSource.scansRemaining

    suspend fun initializeOrResetIfNeeded() {
        scanCounterDataSource.initializeOrResetIfNeeded()
    }

    suspend fun decrementScan() {
        scanCounterDataSource.decrementScan()
    }
}