package io.devexpert.splitbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.splitbill.app.data.scan.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.scan.ScanCounterRepository
import io.devexpert.splitbill.app.data.ticket.MLTicketDataSource
import io.devexpert.splitbill.app.data.ticket.MockTicketDataSource
import io.devexpert.splitbill.data.ticket.TicketRepository
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
                        HomeScreen(
                            ticketRepository,
                            scanCounterRepository = scanCounterRepository,
                            onTicketProcessed = { ticketData ->
                                navController.navigate("receipt")   // Navegar a la pantalla de detalle
                            }
                        )
                    }

                    composable("receipt") {
                        ReceiptScreen(
                            ticketRepository,
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