package io.devexpert.splitbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.splitbill.di.AppModule
import io.devexpert.splitbill.presentation.home.HomeScreen
import io.devexpert.splitbill.presentation.receipt.ReceiptScreen
import io.devexpert.splitbill.presentation.theme.SplitBillTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SplitBillTheme {

                val navController = rememberNavController()
                val context = LocalContext.current

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {

                        HomeScreen(
                            viewmodel = viewModel { AppModule.createHomeViewModel(context) },
                            onTicketProcessed = {
                                navController.navigate("receipt")
                            }
                        )
                    }

                    composable("receipt") {
                        ReceiptScreen(
                            viewModel = viewModel { AppModule.createReceiptViewModel() },
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