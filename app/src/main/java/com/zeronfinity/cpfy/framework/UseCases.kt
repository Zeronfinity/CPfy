package com.zeronfinity.cpfy.framework

import com.zeronfinity.core.usecase.*

data class UseCases(
    val addContestList: AddContestList,
    val getContest: GetContest,
    val getContestCount: GetContestCount,
    val removeAllContests: RemoveAllContests,
    val addPlatform: AddPlatform,
    val getPlatformImageUrl: GetPlatformImageUrl,
    val removeAllPlatforms: RemoveAllPlatforms
)
