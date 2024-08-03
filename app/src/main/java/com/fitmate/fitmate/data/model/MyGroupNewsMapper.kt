package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import com.fitmate.fitmate.domain.model.MyGroupNews

object MyGroupNewsMapper {

    fun MyGroupNewsDto.toMyGroupNews(): MyGroupNews {
        return MyGroupNews(
            fitGroupId = this.fitGroupId,
            certificationRequestUserId = this.certificationRequestUserId,
            certificationRequestUserNickname = this.certificationRequestUserNickname,
            thumbnailEndPoint = this.thumbnailEndPoint,
            certificationStatus = this.certificationStatus,
            agreeCount = this.agreeCount,
            disagreeCount = this.disagreeCount,
            maxAgreeCount = this.maxAgreeCount,
            isUserVoteDone = this.isUserVoteDone,
            isUserAgree = this.isUserAgree,
            issueDate = this.issueDate
        )
    }
}