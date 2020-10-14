package ru.dilgorp.android.travelplanner.navigator

import android.os.Bundle
import androidx.navigation.NavController

interface Navigator {
    fun navigateToDestination(navController: NavController, destinationId: Int, args: Bundle? = null)
    fun navigateWithoutBackStack(navController: NavController, destinationId: Int, args: Bundle? = null)
}