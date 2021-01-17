package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.usecase.SetAllNotificationPriorityUseCase

class NotificationViewModel @ViewModelInject constructor(
    private val setAllNotificationPriorityUseCase: SetAllNotificationPriorityUseCase
) : ViewModel() {

    fun setAllNotificationPriorityList(notificationPriority: String) {
        setAllNotificationPriorityUseCase(notificationPriority)
    }
}
