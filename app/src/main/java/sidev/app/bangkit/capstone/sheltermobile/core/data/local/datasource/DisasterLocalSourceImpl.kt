package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.DisasterEntity
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.DisasterDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class DisasterLocalSourceImpl(private val dao: DisasterDao): DisasterLocalSource {
    override suspend fun getDisasterList(): Result<List<Disaster>> {
        val list = dao.getDisasterList().map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getDisaster(id: Int): Result<Disaster> = dao.getDisaster(id)?.let {
        Success(it.toModel(), 0)
    } ?: Util.noEntityFailResult()

    /**
     * Returns saved count.
     */
    override suspend fun saveDisasterList(list: List<Disaster>): Result<Int> {
        val entityList = list.map { it.toEntity() }
        val insertedCount = dao.saveDisasterList(entityList)
        return Success(insertedCount, 0)
    }
}