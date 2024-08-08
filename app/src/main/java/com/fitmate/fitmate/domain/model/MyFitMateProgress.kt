package com.fitmate.fitmate.domain.model

data class FitMateProgress(
    val fitGroupId: Int,
    val fitGroupName: String,
    val cycle: Int,
    val frequency: Int,
    val fitCertificationProgresses: List<FitMateProgressContent>
)


data class FitMateProgressContent(
    val fitMateUserId: String,
    val fitMateUserNickname: String,
    val certificationCount: Int
)

data class ItemFitMateProgressForAdapter(
    val fitMateUserId: String,
    val frequency: Int,
    val fitMateUserNickname: String,
    val certificationCount: Int
)
