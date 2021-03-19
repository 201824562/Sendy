package com.example.sendytoyproject1


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import com.google.android.material.appbar.AppBarLayout
import androidx.viewpager2.adapter.FragmentStateAdapter as FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, lc:Lifecycle) : FragmentStateAdapter(fm,lc){

    override fun getItemCount(): Int = 2 //PagerViewadapter에서 관리할 View 개수를 반환한다.

    override fun createFragment(position: Int): Fragment{ 
        return when(position){
            0 -> FirstFragment()
            1 -> SecondFragment()
            else -> FirstFragment()
        }
    }

    //추가?


}