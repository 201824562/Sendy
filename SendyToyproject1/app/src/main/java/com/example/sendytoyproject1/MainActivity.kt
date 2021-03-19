package com.example.sendytoyproject1


import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViewPager()

        //NavigationUI.setupWithNavController(main_bottom_navigation, findNavController(R.id.nav_host_fragment))


        //카카오 맵 api 사용 위해서 해시키 등록 필요
        /*try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
            }
        } catch(e: Exception) {
            Log.e("name not found", e.toString())
        }*/

    }


    private fun initViewPager() {

        //main_pager.adapter = ViewPagerAdapter(this) //뷰페이저와 뷰페이저어댑터 연결
        main_pager.offscreenPageLimit = 2                //뷰계층구조에 보관된 페이지, view/fragment 수를 제어할 수 있다.
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        main_pager.adapter = viewPagerAdapter
        main_pager.registerOnPageChangeCallback(PageChangeCallback())
        main_bottom_navigation.setOnNavigationItemSelectedListener { navigationSelected(it) } //바텀네비게이션뷰와 셀렉팅리스너 연결


        /*
        TabLayoutMediator(tab_layout, nav_host_fragment){ tab, position->   //탭레이아웃과 뷰페이저(어댑터) 연결
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconList[position])
        }.attach() */

        main_pager.isUserInputEnabled = false
        
    }

    private fun navigationSelected(item: MenuItem): Boolean {
        val checked = item.setChecked(true)
        when (checked.itemId) {
            R.id.fragment_first_btn -> {
                main_pager.currentItem = 0
                return true
            }
            R.id.fragment_second_btn-> {
                main_pager.currentItem = 1
                return true
            }
        }
        return false
    }

    private inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            main_bottom_navigation.selectedItemId = when (position) {
                0 -> R.id.fragment_first_btn
                1 -> R.id.fragment_second_btn
                else -> error("no such position: $position")
            }
        }
    }

}


