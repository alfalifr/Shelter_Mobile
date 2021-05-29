package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.NewsRepo

class NewsUseCaseImpl(
    private val repo: NewsRepo
): NewsUseCase, NewsRepo by repo