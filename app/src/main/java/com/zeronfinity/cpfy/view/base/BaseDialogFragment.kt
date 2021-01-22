package com.zeronfinity.cpfy.view.base

import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.zeronfinity.core.logger.logE

open class BaseDialogFragment : DialogFragment() {
    fun NavController.safeNavigate(navDirections: NavDirections) {
        try {
            this.navigate(navDirections)
        } catch (ex: Exception) {
            logE(ex.stackTraceToString())
        }
    }
}
