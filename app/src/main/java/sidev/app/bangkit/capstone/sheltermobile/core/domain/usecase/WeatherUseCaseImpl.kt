package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WeatherRepo

class WeatherUseCaseImpl(private val repo: WeatherRepo): WeatherUseCase, WeatherRepo by repo