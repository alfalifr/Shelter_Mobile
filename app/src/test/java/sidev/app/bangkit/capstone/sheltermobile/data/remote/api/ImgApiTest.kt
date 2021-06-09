package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.ImgApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.ImageReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.console.prin
import java.io.File
import java.util.*

class ImgApiTest {
    companion object {
        val api: ImgApi by lazy { AppRetrofit.imgurApi }
    }

    @Test
    fun uploadImgTest(){
        //"D:\\Data\\Pemrograman\\Aktif\\Sertifikasi\\Bangkit\\2021\\Capstone\\Shelter_Mobile\\.res\\raw\\9f68d2168c1ebb5b6368b7ed5b25aeb3.jpg"
        val wdir = System.getProperty("user.dir")
        prin("wdir = $wdir")
/*
        val path2 = "_res/raw"
        prin("\"_res/raw\" exists() = ${File(path2).exists()}")

        val files = File("").listFiles()
        for(f in files){
            prin("f = $f")
        }
 */

        val path = "../_res/raw/9f68d2168c1ebb5b6368b7ed5b25aeb3.jpg"
        val file = File(path)
        val bytes = file.readBytes()
        val base64 = Base64.getEncoder().encode(bytes).decodeToString()

        val req = ImageReqBody(base64)
        val res = api.uploadImg(req).execute()

        prin("res= $res")

        assert(res.isSuccessful)

        val data = res.body()!!

        prin("data= $data")

        assertEquals(Const.CODE_OK, data.status)
        assertNotNull(data.data.link)
    }
}