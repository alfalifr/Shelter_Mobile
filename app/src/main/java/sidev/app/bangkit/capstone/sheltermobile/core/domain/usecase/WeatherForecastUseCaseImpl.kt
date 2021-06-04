package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WeatherForecastRepo

class WeatherForecastUseCaseImpl(private val repo: WeatherForecastRepo): WeatherForecastUseCase, WeatherForecastRepo by repo