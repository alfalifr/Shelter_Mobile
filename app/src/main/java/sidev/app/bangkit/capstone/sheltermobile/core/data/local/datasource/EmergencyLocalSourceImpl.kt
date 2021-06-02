package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.EmergencyDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.console.prine

class EmergencyLocalSourceImpl(private val dao: EmergencyDao): EmergencyLocalSource {
    override suspend fun getAllEmergencies(): Result<List<Emergency>> {
        val list = dao.getAllEmergencies().map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getEmergency(id: Int): Result<Emergency> {
        loge("EmergencyLocalSrc.getEmergency() id= $id")
        val data = dao.getEmergency(id)?.toModel().also {
            loge("EmergencyLocalSrc.getEmergency() model= $it")
        } ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    /**
     * Returns saved count.
     */
    override suspend fun saveEmergencyList(list: List<Emergency>): Result<Int> {
        val entityList = list.map { it.toEntity() }
        val insertedCount = dao.saveEmergencyList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size). also {
            loge("EmergencyLocalSrc.saveEmergencyList() list= $list insertedCount= $insertedCount entityList.size= ${entityList.size} res= $it")
        }
    }
}