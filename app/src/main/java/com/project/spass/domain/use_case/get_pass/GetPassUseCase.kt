package com.project.spass.domain.use_case.get_pass

import com.project.spass.common.Resource
import com.project.spass.domain.model.PassModel
import com.project.spass.domain.repository.PassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPassUseCase @Inject constructor(
    private val repository: PassRepository
) {
    operator fun invoke(): Flow<Resource<List<PassModel>>> = flow {
        try {
            emit(Resource.Loading())
            val passes = repository.getPasses()?.map { it }
            emit(Resource.Success(data = passes))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}