package com.project.spass.domain.use_case.get_pass_detail

import com.project.spass.common.Resource
import com.project.spass.domain.model.PassModel
import com.project.spass.domain.repository.PassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPassDetailUseCase @Inject constructor(private val repository: PassRepository) {
    operator fun invoke(passId: Int): Flow<Resource<PassModel>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.getPassDetail(passId)
            emit(Resource.Success(data = data))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

}