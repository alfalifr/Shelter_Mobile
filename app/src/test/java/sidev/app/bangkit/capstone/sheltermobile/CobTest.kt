package sidev.app.bangkit.capstone.sheltermobile

import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import sidev.lib.console.prin

class CobTest {
    @Test
    fun disasterGroupListTest(){
        val disaster = Dummy.disasterList[0]
        val warningList = Dummy.warningList1
        val g1 = DisasterGroup(disaster, warningList)
        val g2 = DisasterGroup(disaster, warningList)

        val list = listOf(g2)

        val index = list.indexOf(g1)

        prin("list = $list \nindex= $index")
    }
}