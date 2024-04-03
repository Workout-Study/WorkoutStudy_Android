package com.fitmate.fitmate.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fitmate.fitmate.data.model.entity.CertificationEntity
import com.fitmate.fitmate.domain.model.DbCertification
import kotlinx.coroutines.flow.Flow

@Dao
interface CertificationDao {
    //인증 데이터 전부 가져오기(항상 값은 하나임.)
    @Query("SELECT * FROM Certification")
    fun selectAll(): Flow<List<CertificationEntity>>

    //아이디가 1인 데이터만 뽑는 쿼리문
    @Query("SELECT * FROM Certification WHERE id = :id")
    fun selectOne(id:Int): CertificationEntity?

    //인증 데이터 삭제
    @Query("DELETE FROM Certification")
    suspend fun delete()

    //인증 데이터 삽입(혹시 몰라서 REPLACE 작업)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CertificationEntity)

}
