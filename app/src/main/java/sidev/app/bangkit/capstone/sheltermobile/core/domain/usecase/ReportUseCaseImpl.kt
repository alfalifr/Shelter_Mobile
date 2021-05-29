package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo

class ReportUseCaseImpl(private val repo: ReportRepo): ReportUseCase, ReportRepo by repo