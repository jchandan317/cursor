package com.pgsc.tracko.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pgsc.tracko.presentation.login.LoginScreen
import com.pgsc.tracko.presentation.ticket.list.TicketListScreen

@Composable
fun TrackoNavGraph(
    navController: NavHostController = rememberNavController()
): NavHostController {
    NavHost(
        navController = navController,
        startDestination = Routes.Login
    ) {
        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.TicketList) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.TicketList) {
            TicketListScreen(navController = navController)
        }
        composable(Routes.CreateTicket) {
            TicketListScreen(navController = navController)
        }
        composable(
            route = Routes.TicketDetail.pattern,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) {
            TicketListScreen(navController = navController)
        }
    }
    return navController
}
