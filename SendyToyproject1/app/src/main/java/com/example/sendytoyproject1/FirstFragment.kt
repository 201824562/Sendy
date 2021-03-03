package com.example.sendytoyproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.fragment_first.*
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment :  Fragment(), OnMapReadyCallback {

    private lateinit var locationSource : FusedLocationSource
    private lateinit var naverMap: NaverMap
    var mNow: Long = 0
    var mDate: Date? = null
    var mFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )) {
            if (!locationSource.isActivated) { // 권한 거부됨(위치추적 x)
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
                                                //왜 여기서는 위치따라가게 설정안되나? (else면 밑에 넣는 거랑 같지않나..? -> 생명주기 이해 부족?)
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {   //이게 oncreated같은 개념 맞나? -> 그래서 여기 버튼 onclick 넣음
        Toast.makeText(context, "지도 준비 완료", Toast.LENGTH_SHORT).show()

        // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        this.naverMap = naverMap

        naverMap.locationSource = locationSource //locationSource!= latitude (따라서 좌표값 얻기 위해서 오버레이가  필요하다.)
        naverMap.locationTrackingMode = LocationTrackingMode.Follow     //권한 허용시(카메라 따라가게 모드 설정 o)

        // 지도상에 마커 표시(참고자료)
        /*
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = naverMap
    }*/

        //플로팅 버튼 -> 데이터 기록용
        sendButton.setOnClickListener { view ->
            Snackbar.make(view, "시간 " +getTime() + "과  위치 " + locationSource + "가 출근부에 기록되었습니다.", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    companion object {//한 클래스의 인스턴트 간 공유데이터
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000 //리퀘타임_상수
    }

    private fun getTime(): String? {
        mNow = System.currentTimeMillis()
        mDate = Date(mNow)
        return mFormat.format(mDate)
    }

}
