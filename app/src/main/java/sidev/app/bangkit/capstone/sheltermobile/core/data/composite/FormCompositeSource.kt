package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.FormLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.FormRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.FormRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import java.lang.IllegalArgumentException

class FormCompositeSource(
    private val localSrc: FormLocalSource,
    private val remoteSrc: FormRemoteSource,
): CompositeDataSource<Form>(), FormRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Form>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Form>> = getDataList(remoteSrc, args)

    suspend fun getDataList(repo: FormRepo, args: Map<String, Any?>): Result<List<Form>> {
        val id = args[Const.KEY_TIMESTAMP] as? String ?: throw IllegalArgumentException("args[Const.KEY_TIMESTAMP] == null")
        return repo.getForm(id).toListResult()
    }

    override suspend fun saveDataList(remoteDataList: List<Form>): Result<Int> = when(val res = localSrc.saveForm(remoteDataList.first())) { //TODO ALIF 31 Mei 2021
        is Success -> Success(1, 0)
        is Fail -> res
    }


    override suspend fun getForm(timestamp: String): Result<Form> = getDataList(mapOf(
        Const.KEY_TIMESTAMP to timestamp
    )).toSingleResult()

    override suspend fun saveForm(data: Form): Result<Boolean> = when(val res = saveDataList(listOf(data))){
        is Success -> Success(true, 0)
        is Fail -> res
    }
}