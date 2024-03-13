package com.project.spass.data.repository

import com.project.spass.data.demo_db.DemoDB
import com.project.spass.domain.model.PassModel
import com.project.spass.domain.repository.PassRepository
import javax.inject.Inject

class PassRepositoryImp @Inject constructor(
    private val demoDB: DemoDB
) : PassRepository {
    override suspend fun getPasses(): List<PassModel> {
        return demoDB.getPasses()
    }

    override suspend fun getPassDetail(id: Int): PassModel {
        return demoDB.getPasses()[id-1]
    }
}