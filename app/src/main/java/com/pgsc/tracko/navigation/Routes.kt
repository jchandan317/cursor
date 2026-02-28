package com.pgsc.tracko.navigation

object Routes {
    const val Login = "login"
    const val TicketList = "tickets"
    const val CreateTicket = "tickets/create"

    object TicketDetail {
        private const val base = "tickets"
        fun createRoute(id: String) = "$base/$id"
        const val pattern = "$base/{ticketId}"
        fun parseId(route: String?): String? = route?.substringAfter("$base/")?.takeIf { it.isNotEmpty() }
    }
}
