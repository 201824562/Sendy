package com.example.sendytoyproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource


class FirstFragment :  Fragment(), OnMapReadyCallback {

    private lateinit var locationSource : FusedLocationSource
    private lateinit var naverMap: NaverMap

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

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        //FusedLocationSource : 런타임 권한 처리함, 이때 액티비티 혹은 프레그먼트 필요. (생성자에 액티비티나 프래그먼트 객체를 전달하고 권한 요청 코드를 지정해야함.)
        //onRequestPermissionResult()의 결과를 FusedLocationSource의 onRequestPermissionsResult()에 전달함.

        return rootView     //뷰 리턴.
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                        grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨(위치추적 x)
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            else { //위치가 따라감.(모드 4개중 Follow)
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        Toast.makeText(context, "지도 준비 완료", Toast.LENGTH_SHORT).show()

        // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        // 지도상에 마커 표시
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = naverMap
    }

    companion object {//한 클래스의 인스턴트 간 공유데이터
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000 //리퀘타임_상수
    }


}
