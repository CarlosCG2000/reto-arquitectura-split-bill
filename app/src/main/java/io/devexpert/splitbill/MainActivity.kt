package io.devexpert.splitbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.splitbill.app.data.scan.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.scan.ScanCounterRepository
import io.devexpert.splitbill.app.data.ticket.MLTicketDataSource
import io.devexpert.splitbill.app.data.ticket.MockTicketDataSource
import io.devexpert.splitbill.data.ticket.TicketRepository
import io.devexpert.splitbill.domain.useCases.DecrementScanCounterUseCase
import io.devexpert.splitbill.domain.useCases.GetScansRemainingUseCase
import io.devexpert.splitbill.domain.useCases.GetTicketDataUseCase
import io.devexpert.splitbill.domain.useCases.InitializeScanCounterUseCase
import io.devexpert.splitbill.domain.useCases.ProcessTicketUseCase
import io.devexpert.splitbill.ui.home.HomeScreen
import io.devexpert.splitbill.ui.home.HomeViewModel
import io.devexpert.splitbill.ui.receipt.ReceiptScreen
import io.devexpert.splitbill.ui.receipt.ReceiptViewModel
import io.devexpert.splitbill.ui.theme.SplitBillTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataSource = if(BuildConfig.DEBUG) MockTicketDataSource()
                         else MLTicketDataSource()

        val ticketRepository = TicketRepository(dataSource)

        val dataSourceScan = DataStoreScanCounterDataSource(this)
        val scanCounterRepository = ScanCounterRepository(dataSourceScan)

        setContent {
            SplitBillTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {

                        val homeViewModel: HomeViewModel = viewModel {
                            HomeViewModel(
                                processTicketUseCase = ProcessTicketUseCase(ticketRepository),
                                initializeScanCounterUseCase = InitializeScanCounterUseCase(scanCounterRepository),
                                getScansRemainingUseCase = GetScansRemainingUseCase(scanCounterRepository),
                                decrementScanCounterUseCase = DecrementScanCounterUseCase(scanCounterRepository)
                            )
                        }

                        HomeScreen(
                            viewmodel = homeViewModel,
                            onTicketProcessed = {
                                navController.navigate("receipt")   // Navegar a la pantalla de detalle
                            }
                        )
                    }

                    composable("receipt") {

                        val ticketViewModel = viewModel {
                            ReceiptViewModel(
                                getTicketDataUseCase = GetTicketDataUseCase(ticketRepository)
                            )
                        }

                        ReceiptScreen(
                            viewModel = ticketViewModel,
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}