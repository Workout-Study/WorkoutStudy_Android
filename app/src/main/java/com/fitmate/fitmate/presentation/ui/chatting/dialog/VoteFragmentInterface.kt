package com.fitmate.fitmate.presentation.ui.chatting.dialog

import com.fitmate.fitmate.domain.model.VoteRequest

interface VoteFragmentInterface {
    abstract var userId: Int

    fun fetchVoteData()

    fun postVote(voteItem: VoteRequest)

    fun putVote(voteItem: VoteRequest)
}