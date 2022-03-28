package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Boolean> = repository.isUserLoggedIn()
}