package com.example.sendytoyproject1

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.Map.KaKaoAPI
import com.example.sendytoyproject1.Map.PlaceDocument
import com.example.sendytoyproject1.Map.ResultSearchKeyword
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment :  Fragment(), OnMapReadyCallback {

    private val viewModel: LocationViewmodel by viewModels()    //'by viewModels()' -> (만들어둔) 뷰모델을 사용하기 위해 접근하는 툴 (viewmodel 수명 != 프레그 수명)
    private lateinit var binding: FragmentFirstBinding

    private var locationSource : FusedLocationSource ? = null
    private var naverMap: NaverMap ? = null
    private var locationManager : LocationManager ?= null //LocationManager
    private var connectionManager : ConnectivityManager ?= null //WifiManager

    companion object {
        private const val TAG = "Map"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000 //리퀘타임_상수
        private val PERMISSIONS = arrayOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    //검색 결과용 변수들.
    private var places : List<PlaceDocument>?= null
    private var near: LatLng? = null //좌표인듯.

    //주소 변환용 변수들.
    private var nowLat : Double = 35.157662
    private var nowLng : Double = 129.059111
    private var addresslist: List<Address>? = null
    private var address : String? = null

    //날짜/시간용 변수들.
    private var mNow: Long = 0
    private var mDate: Date? = null
    private var mFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")


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

        find()

        val rootView : View = binding.root
        return rootView     //뷰 리턴. (==binding.root / 액티비티뷰 리턴)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        // binding.viewModel = viewModel -> 용도 모르겠음 + 여기서 왜 안되는건지도 모르겠음.

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                findStore(query!!)

                if(near!= null){
                    //logic 짜기.
                }

                return true //클릭리스너로 커스텀하려면 (true) 리턴.
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // request code와 권한획득 여부 확인
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) { //위치 허용 O (!= 위치 on이랑 다름)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap?.locationTrackingMode = LocationTrackingMode.Follow
            }
            else naverMap?.locationTrackingMode = LocationTrackingMode.None
            return
        }
    }

    override fun onMapReady(naverMap: NaverMap) {   //이게 oncreated같은 개념 맞나? -> 그래서 여기 버튼 onclick 넣음

        requestPermissions(PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE              )
        Toast.makeText(context, "지도 준비 완료", Toast.LENGTH_SHORT).show()

        findLocation(naverMap)

        //현재위치 찾아주는 버튼이다.
        findlocationButton.setOnClickListener{
            findLocation(naverMap)
        }


        //사용자의 위치가 변경될 경우 호출되는 콜백 메서드
        naverMap.addOnLocationChangeListener { location ->
            nowLat = location.latitude
            nowLng = location.longitude
        }

        //플로팅 버튼 -> 데이터 기록용                             //추가 필요: 이미 등록된 경우 등록되지 않게 해주어야함.
        sendButton.setOnClickListener { view ->

            val geoCoder = Geocoder(context, Locale.getDefault())
            locationManager = (context)!!.getSystemService()
            connectionManager = (context)!!.getSystemService()

            addresslist =  geoCoder.getFromLocation(nowLat, nowLng, 1) //위도,경도 -> 주소로 변환

            if (CheckServiceState()) SendButtonClick(view)

        }
    }

    private fun CheckServiceState() : Boolean {
        if (!locationManager!!.isLocationEnabled && connectionManager!!.activeNetwork==null){  //GPS나 네트워크가 Off 일때
            Toast.makeText(context, "현재 위치 정보 확인을 위해 Wifi와 GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
            return false }
        else if (!locationManager!!.isLocationEnabled ) {
            Toast.makeText(context, "현재 위치 정보 확인을 위해 GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (connectionManager!!.activeNetwork==null){
            Toast.makeText(context, "현재 위치 정보 확인을 위해 Wifi를 켜주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            return true
        }
    }

    private fun getTime(): String? {
        mNow = System.currentTimeMillis()
        mDate = Date(mNow)
        return mFormat.format(mDate)
    }

    // (데이터바인딩) 플로팅 버튼 온클릭
    fun SendButtonClick(view : View){
        //버튼 눌렀을 때 로직 짜기! (바인딩 된 데이터를 저장해야함)
        viewModel.insert(LocationItemData(getTime().toString(), address.toString(), "",0,false))

        address = addresslist!!.get(0).getAddressLine(0) //geoCoder가 잡은 주소가 없지 않을 때!

        Snackbar.make(
            view,
            "시간:" + getTime() + ", 위치:" + ( address.toString()) + "가 출근부에 기록되었습니다.",
            Snackbar.LENGTH_LONG
        ).show()

        // 출근부 위치 -> 지도상에 마커 표시 (찍은 것만 뜨게 하자..)  귀찮다..ㅎ
        val marker = Marker()
        marker.position = LatLng(nowLat, nowLng)
        marker.map = naverMap
    }

    private fun findLocation(received_naverMap: NaverMap){
        // NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        if (received_naverMap != null ){

            this.naverMap = received_naverMap
            naverMap!!.locationSource = locationSource // 초기 위치 설정(주의:locationSource!= latitude)
            naverMap!!.locationTrackingMode = LocationTrackingMode.Follow     //권한 허용시(카메라 따라가게 모드 설정 o)
        }
    }

    private fun find(){
        val api = KaKaoAPI.create()

        //API 서버에 요청 (추가 : 여기서 "음식점" 말고 입력받은 edittext 값을 긁어와서 넣어주게 해주어야 한다.)
        api.getSearchLocation("음식점",  nowLng, nowLat).enqueue(object : Callback<ResultSearchKeyword> {

            //통신 성공
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                //(추가공부) 여기서 meta로 places도 List화 해주어서 갈아끼워줘야함.
                places = response.body()?.documents     //여기서 바로 출력 x. 저장해야댐.

                Log.d("Test", "${places.toString()}")
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}")

                for(place in places!!) {
                    val marker = Marker()   //마커 표시용.
                    val location = LatLng(place.y!!, place.x!!)
                    marker.position = location
                    marker.captionText = place.place_name
                    marker.captionColor = Color.rgb(2,186,133)
                    marker.map = naverMap
                }
            }

            //통신 실패
            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                Log.e(TAG,"FAIL")
            }
        })
    }


    //추가 : 다시 구현하기
    private fun findStore(keyword: String) {  //직접 입력한 하나의 집으로 이동.(with animation)
        near = null //near을 다시 비워줌.

        for(place in places!!) {
            val name = place.place_name
            if(name.contains(keyword)) {
                near = LatLng(place.y!!, place.x!!) //LatLng==위도,경도를 좌표로 만든 좌표생성자.
                break
            }
        }
    }//findStore



}
