package com.zeronfinity.cpfy.view

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.zeronfinity.core.logger.logE

open class BaseFragment : Fragment() {
    fun NavController.safeNavigate(navDirections: NavDirections) {
        try {
            this.navigate(navDirections)
        } catch (ex: Exception) {
            logE(ex.stackTraceToString())
        }
    }
}
