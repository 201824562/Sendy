package com.example.sendytoyproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class FirstFragment :  Fragment(), OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_first, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?  //코틀린 extension으로 못하려나?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            } //if mapFragment == null, make MapFragment' new instance and put it in the 'mapFragment'.  And then begin Transaction and comit.(내해석)



        mapFragment.getMapAsync(this) // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출 -> onMapReady에서 NaverMap 객체를 받음 => "객체 sync함."

        return rootView     //뷰 리턴.
    }

    override fun onMapReady(naverMap: NaverMap) {
        Toast.makeText(context, "지도 준비 완료", Toast.LENGTH_SHORT).show()

        // 지도상에 마커 표시
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = naverMap

    }
}