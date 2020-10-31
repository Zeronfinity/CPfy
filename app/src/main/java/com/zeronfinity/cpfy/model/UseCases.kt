package com.zeronfinity.cpfy.model

import com.zeronfinity.core.usecase.*

data class UseCases(
    val addContestListUseCase: AddContestListUseCase,
    val getContestUseCase: GetContestUseCase,
    val getContestCountUseCase: GetContestCountUseCase,
    val removeAllContestsUseCase: RemoveAllContestsUseCase,
    val addPlatformUseCase: AddPlatformUseCase,
    val addPlatformListUseCase: AddPlatformListUseCase,
    val getPlatformImageUrlUseCase: GetPlatformImageUrlUseCase,
    val removeAllPlatformsUseCase: RemoveAllPlatformsUseCase,
    val fetchServerContestInfoUseCase: FetchServerContestInfoUseCase
)
