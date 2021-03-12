package com.example.sendytoyproject1

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.sendytoyproject1.Data.LocationEntity
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.fragment_first.*
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment :  Fragment(), OnMapReadyCallback {

    private val viewModel: LocationViewmodel by viewModels()    //'by viewModels()' -> (만들어둔) 뷰모델을 사용하기 위해 접근하는 툴
    private lateinit var binding: FragmentFirstBinding

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000 //리퀘타임_상수
    private lateinit var locationSource : FusedLocationSource
    private lateinit var naverMap: NaverMap
    var mNow: Long = 0
    var mDate: Date? = null
    var mFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var nowLat : Double = 0.0
    var nowLng : Double = 0.0
    var addresslist: List<Address>? = null
    var address : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val rootView = inflater.inflate(R.layout.fragment_first, container, false)
        binding  = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_first,  container, false) //뷰->뷰모델


        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?  //코틀린 extension으로 못하려나?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            } //if mapFragment == null, make MapFragment' new instance and put it in the 'mapFragment'.  And then begin Transaction and comit.(내해석)

        mapFragment.getMapAsync(this) // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출 -> onMapReady에서 NaverMap 객체를 받음 => "객체 sync함."

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        //FusedLocationSource : 런타임 권한 처리함, 이때 액티비티 혹은 프레그먼트 필요. (생성자에 액티비티나 프래그먼트 객체를 전달하고 권한 요청 코드를 지정해야함.)
        //onRequestPermissionResult()의 결과를 FusedLocationSource의 onRequestPermissionsResult()에 전달함.

        val rootView : View = binding.root
        return rootView     //뷰 리턴. (==binding.root / 액티비티뷰 리턴)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        // binding.viewModel = viewModel -> 용도 모르겠음 + 여기서 왜 안되는건지도 모르겠음.
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
        naverMap.locationSource = locationSource // 초기 위치 설정(주의:locationSource!= latitude)
        naverMap.locationTrackingMode = LocationTrackingMode.Follow     //권한 허용시(카메라 따라가게 모드 설정 o)


        //사용자의 위치가 변경될 경우 호출되는 콜백 메서드
        naverMap.addOnLocationChangeListener { location ->
            nowLat = location.latitude
            nowLng = location.longitude
        }


        //플로팅 버튼 -> 데이터 기록용                             //추가 필요: 이미 등록된 경우 등록되지 않게 해주어야함.
        sendButton.setOnClickListener { view ->

            var geoCoder = Geocoder(context, Locale.getDefault())
            addresslist =  geoCoder.getFromLocation(nowLat, nowLng, 1) //위도,경도 -> 주소로 변환
            Log.d(TAG, addresslist.toString())
            address = addresslist?.get(0)?.getAddressLine(0)

            Snackbar.make(
                view,
                "시간:" + getTime() + ", 위치:" + ( address.toString()) + "가 출근부에 기록되었습니다.",
                Snackbar.LENGTH_LONG
            ).show()

            Log.d(TAG, address.toString())

            // 출근부 위치 -> 지도상에 마커 표시
            val marker = Marker()
            marker.position = LatLng(nowLat, nowLng)
            marker.map = naverMap

            SendButtonClick(view)
        }
    }


    private fun getTime(): String? {
        mNow = System.currentTimeMillis()
        mDate = Date(mNow)
        return mFormat.format(mDate)
    }

    // (데이터바인딩) 플로팅 버튼 온클릭
    fun SendButtonClick(view: View){
        //Log.d(TAG, "btnClick")

        //버튼 눌렀을 때 로직 짜기! (바인딩 된 데이터를 저장해야함)
        getTime()?.let { LocationEntity(it, address.toString() ) }?.let { viewModel.insert(it) }    //이렇게 하는 거 맞는지 질문하기.

    }

}
