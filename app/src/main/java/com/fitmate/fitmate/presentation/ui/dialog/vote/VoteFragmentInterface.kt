package com.fitmate.fitmate.presentation.ui.dialog.vote

import com.fitmate.fitmate.domain.model.VoteRequest

interface VoteFragmentInterface {
    abstract var userId: Int

    fun fetchVoteData()

    fun postVote(voteItem: VoteRequest)

    fun putVote(voteItem: VoteRequest)
}