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
import java.lang.IllegalArgumentException

class FormCompositeSource(
    private val localSrc: FormLocalSource,
    private val remoteSrc: FormRemoteSource,
): CompositeDataSource<Form>(), FormRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Form>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Form>> = getDataList(remoteSrc, args)

    suspend fun getDataList(repo: FormRepo, args: Map<String, Any?>): Result<List<Form>> {
        val id = args[Const.KEY_ID] as? Int ?: throw IllegalArgumentException("args[Const.KEY_ID] == null")
        return repo.getForm(id).toListResult()
    }

    override suspend fun saveDataList(remoteDataList: List<Form>): Result<Int> = when(val res = localSrc.saveForm(remoteDataList.first())) {
        is Success -> Success(1, 0)
        is Fail -> res
    }
}