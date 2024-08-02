package com.fitmate.fitmate.presentation.ui.chatting.dialog

interface VoteFragmentInterface {
    abstract var userId: Int

    fun fetchVoteData()

    fun postVote()

    fun putVote()
}