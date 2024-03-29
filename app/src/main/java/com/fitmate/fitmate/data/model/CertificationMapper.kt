package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.entity.CertificationEntity
import com.fitmate.fitmate.domain.model.DbCertification
import java.net.URI
import java.time.Instant

object CertificationMapper {

    fun DbCertification.toEntity() = CertificationEntity(
        id = id ?: -1,
        userId = userId,
        recordStartDate = recordStartDate,
        recordEndDate = recordEndDate ?: Instant.now(),
        startImages = startImages,
        endImages = endImages ?: emptyList<URI>().toMutableList()
    )

    fun CertificationEntity.toDbCertification() = DbCertification(
        id = id,
        userId = userId,
        recordStartDate = recordStartDate,
        recordEndDate = recordEndDate,
        startImages = startImages,
        endImages = endImages
    )


}