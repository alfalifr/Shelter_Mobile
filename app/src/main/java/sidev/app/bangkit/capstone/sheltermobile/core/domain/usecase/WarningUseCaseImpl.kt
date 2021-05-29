package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WarningRepo

class WarningUseCaseImpl(private val repo: WarningRepo): WarningUseCase, WarningRepo by repo