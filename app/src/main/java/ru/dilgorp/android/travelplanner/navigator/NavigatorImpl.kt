package ru.dilgorp.android.travelplanner.navigator

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.dilgorp.android.travelplanner.R
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToDestination(
        navController: NavController,
        destinationId: Int,
        args: Bundle?
    ) {
        val navOptions =
            NavOptions.Builder()
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .setLaunchSingleTop(true).build()
        navController.navigate(destinationId, args, navOptions)
    }

    override fun navigateWithoutBackStack(
        navController: NavController,
        destinationId: Int,
        args: Bundle?
    ) {
        val currentDestination = navController.currentDestination

        val navOptions =
            NavOptions.Builder()
                .setPopUpTo(currentDestination!!.id, true)
                .setLaunchSingleTop(true)
                .build()

        navController.navigate(destinationId, args, navOptions)
    }

}