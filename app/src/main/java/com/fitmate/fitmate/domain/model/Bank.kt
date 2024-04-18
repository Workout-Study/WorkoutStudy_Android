package com.fitmate.fitmate.domain.model

data class BankList(
    val banks: List<Bank>
)
data class Bank(
    val value: String?,
    val key: String
)
