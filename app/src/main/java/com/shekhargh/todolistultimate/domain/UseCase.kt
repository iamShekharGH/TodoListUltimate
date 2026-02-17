package com.shekhargh.todolistultimate.domain

interface SuspendUseCase<in I, out O> {
    suspend operator fun invoke(input: I): O
}

interface SuspendUseCaseMultipleInput<in I, in I2, out O> {
    suspend operator fun invoke(input: I, input2: I2): O
}

interface UseCase<in I, out O> {
    operator fun invoke(param: I): O
}

interface NoParameterUseCase<out O> {
    operator fun invoke(): O
}