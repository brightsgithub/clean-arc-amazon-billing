package com.example.domain.usecase.interfaces

interface UseCase<in Params, out ReturnType> {
    operator fun invoke(params: Params): ReturnType
}

interface UseCaseUnit<out ReturnType> {
    operator fun invoke(): ReturnType
}

interface SuspendedUseCase<in Params, out ReturnType> {
    suspend operator fun invoke(params: Params): ReturnType
}

interface SuspendedUseCaseUnit<out ReturnType> {
    suspend operator fun invoke(): ReturnType
}
