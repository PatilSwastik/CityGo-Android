package com.project.spass.domain.repository

import com.project.spass.domain.model.PassModel

interface PassRepository {
    suspend fun getPasses(): List<PassModel>? = null
    suspend fun getPassDetail(id: Int): PassModel? = null
}