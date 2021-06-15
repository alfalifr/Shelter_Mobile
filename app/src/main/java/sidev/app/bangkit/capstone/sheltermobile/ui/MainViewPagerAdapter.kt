package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalStateException

class MainViewPagerAdapter(act: FragmentActivity): FragmentStateAdapter(act) {
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = 4

    /**
     * Provide a new Fragment associated with the specified position.
     *
     *
     * The adapter will be responsible for the Fragment lifecycle:
     *
     *  * The Fragment will be used to display an item.
     *  * The Fragment will be destroyed when it gets too far from the viewport, and its state
     * will be saved. When the item is close to the viewport again, a new Fragment will be
     * requested, and a previously saved state will be used to initialize it.
     *
     * @see ViewPager2.setOffscreenPageLimit
     */
    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> HomeFragment()
        1 -> LaporFragment()
        2 -> ArticleNewsFragment()
        3 -> PengaturanFragment()
        else -> throw IllegalStateException("No such page index ($position)")
    }
}