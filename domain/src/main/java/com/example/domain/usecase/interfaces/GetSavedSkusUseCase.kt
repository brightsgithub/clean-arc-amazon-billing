package com.example.domain.usecase.interfaces

import com.example.domain.exceptions.NoSavedSkusFoundException
import com.example.domain.models.NowSku
import kotlin.jvm.Throws

interface GetSavedSkusUseCase: SuspendedUseCaseUnit<List<NowSku>> {
    @Throws(NoSavedSkusFoundException::class)
    override suspend fun invoke(): List<NowSku>
}