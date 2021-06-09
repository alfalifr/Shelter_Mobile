package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.LocationDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

class LocationLocalSourceImpl(private val dao: LocationDao, private val ctx: Context): LocationLocalSource {
    override suspend fun getCurrentLocation(): Result<Location> {
        val dataId = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        if(dataId < 0)
            return Util.noValueFailResult()

        loge("LocLocalSrc getCurrentLocation() dataId = $dataId")
        return getLocationById(dataId)
    }

    override suspend fun getLocationById(id: Int): Result<Location> {
        val data = dao.getLocationById(id)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun getAllLocation(): Result<List<Location>> {
        val list = dao.getAllLocation().map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> {
        val data = dao.getLocation(coordinate)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun getLocationByName(name: String): Result<Location> {
        loge("dao.getLocationByName(name) name= $name")
        val data = dao.getLocationByName(name)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun saveCurrentLocation(data: Location): Result<Boolean> {
        Util.editSharedPref(ctx, true) {
            putInt(Const.KEY_LOCATION_ID, data.id)
            loge("LocLocalSrc saveCurrentLocation() data = $data")
        }
        return Success(true, 0)
    }

    override suspend fun saveLocation(data: Location): Result<Boolean> {
        val rowId = dao.saveLocation(data.toEntity(-1))
        return if(rowId >= 0L) Success(true, 0)
        else Util.cantInsertFailResult()
    }

    override suspend fun saveLocationList(list: List<Location>): Result<Int> {
        val entityList = list.map { it.toEntity(-1) }
        val insertedCount = dao.saveLocationList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}