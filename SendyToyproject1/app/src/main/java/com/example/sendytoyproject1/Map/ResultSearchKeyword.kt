package com.example.sendytoyproject1.Map

import com.google.gson.annotations.SerializedName

//[Response] 가져와지는 Real 정보들 조건!(중요)
//검색결과를 담는 클래스(정보를 담는 '틀'!!!!!!!) -> Request 응답 바디.
data class ResultSearchKeyword (
    val meta: PlaceMeta, //장소 메타 데이터
    val documents: List<PlaceDocument>  //검색 결과
)



//장소 메타 데이터
data class PlaceMeta(
    val total_count: Int,   //검색어에 검색된 문서(document) 수
    val pageable_count : Int,   //total_count 중 노출 가능한 문서 수, 최대 45 (API 제공기준)
    val is_end : Boolean       //현재 페이지가 마지막 페이지인지 여부, 값이 false이면 page를 증가시켜 다음 페이지 요청 가능.
   // val same_name : RegionInfo
)

data class RegionInfo (
    val region : List<String>,
    val keyword : String,
    val selected_region : String
    )


//@SerializedName -> 변수명과 Json key값을 다르게 사용하고 싶은 경우 (어노테이션) 사용.
data class PlaceDocument (
    //@SerializedName("address_name")
    val id: String = "",
    val address_name: String = "",
    val category_group_code: String = "", //중요 카테고리만 그룹핑한 카테고리 '그룹 코드'
    val category_group_name: String = "", //중요 카테고리만 그룹핑한 카테고리 '그룹명'
    val category_name: String = "",
    val distance: String = "",
    val phone: String = "",
    val place_name: String = "",
    val place_url: String = "",    //장소 상세페이지 URL
    val road_address_name: String = "",
    val x: Double? = null, // longitude
    val y: Double? = null //  latitude
)

//(추가공부) 페이징으로 15개 이상받아오기?