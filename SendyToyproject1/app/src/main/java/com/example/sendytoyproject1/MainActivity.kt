package com.example.sendytoyproject1


import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
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

        val tabLayoutTextArray = arrayOf("위치", "출근부")
        val tabLayoutIconList = arrayListOf(R.drawable.first_icon, R.drawable.second_icon)

        nav_host_fragment.adapter = ViewPagerAdapter(this) //뷰페이저와 뷰페이저어댑터 연결
        nav_host_fragment.offscreenPageLimit = 2                //뷰계층구조에 보관된 페이지, view/fragment 수를 제어할 수 있다.

        TabLayoutMediator(tab_layout, nav_host_fragment){ tab, position->   //탭레이아웃과 뷰페이저(어댑터) 연결
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconList[position])
        }.attach()

        /*
        //(안드로이드) onTouch -> onClick -> onLongClick 순서로 이벤트가 발생 / return true->이후 발생하는 리스너 이벤트 수행 X.
        nav_host_fragment.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }

         })
         */

        
    }

}


