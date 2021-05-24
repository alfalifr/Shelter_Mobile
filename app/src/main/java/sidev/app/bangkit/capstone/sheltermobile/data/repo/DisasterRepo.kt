package sidev.app.bangkit.capstone.sheltermobile.data.repo

import sidev.app.bangkit.capstone.sheltermobile.data.model.Disaster

interface DisasterRepo {
    suspend fun getDisasterList(): List<Disaster>
}