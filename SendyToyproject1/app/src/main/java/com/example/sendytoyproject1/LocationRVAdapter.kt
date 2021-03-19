package com.example.sendytoyproject1

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.LocationItemBinding
import kotlinx.android.synthetic.main.location_item.view.*


class LocationRVAdapter(receivedlocationdata: List<LocationItemData>,
                        var itemClick: (LocationItemData, cardview: CardView, SparseBooleanArray) -> Unit): RecyclerView.Adapter<LocationRVAdapter.SearchViewHolder>()  {

    private lateinit var binding: LocationItemBinding //(중요) xml에 바인딩한 아이의 이름!

    var locationitems: List<LocationItemData> = receivedlocationdata //(중요) "앱 저장소에서 받아온 데이터 담아줌" -> 매개변수 형식으로 받아온 데이터를 똑같은 형식으로 내부서 재선언.
    var clickeditemsList = SparseBooleanArray()


    companion object{   //변하지 않아야 하는 값.(static)
        var totalItemCounts : Int =0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : SearchViewHolder { //Viewholder에 '뷰'를 넘기는 경우(혹은 여기서 바인딩 해주고 바인딩만 넘겨줘도 됨.)
        binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("LocationRVAdapter", "getItemcount 함수 작동")
        totalItemCounts = locationitems.size
        return totalItemCounts
    }

    //customizing한 뷰를 홀더에 Binding해준다.
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val locationData = locationitems[position] //생성한 뷰홀더를 바인딩 해준다.(개별)
        holder.bind(locationData, totalItemCounts)
    }


    //뷰 홀더 -> 뷰 하나짜리의 구성 ('생성할 뷰' 형식 지정 부분)_커스텀 부분
    inner class SearchViewHolder(binding: LocationItemBinding) : RecyclerView.ViewHolder( //location_item.xml에 연동시켜서 뷰홀더를 customizing하였음.
            binding.root ) //==parent view
    {
        //location_item -> itemView / xml의 뷰 이름 가져옴.
        //val DateTime = itemView.Datetime_text -> 데이터 바인딩으로 안쓰게 되었음.
        //val Location = itemView.locationinfo_text
        val cardview = itemView.cardview

        fun bind(data : LocationItemData, totalItemCounts: Int){ //(커스텀) 하나씩 바인딩 해줌. -> 뷰 하나==아이템 '하나'
            //DateTime.text = locationitem.datetime
            //Location.text = locationitem.address
            binding.locationitem = data


            //뷰 "개인"의 클릭리스너(쓰지 말고 프레그먼트로 넘긴 클릭 인터페이스 사용하기!)
            itemView.setOnClickListener{/*
                //클릭된 아이템의 경우 뷰의 배경색을 바꾸게 설정하는 코드.
                if (liveclickedstate == true){ //선택이 되는 모드일 경우
                    if (data.clickedValue){ //true일 경우
                        data.clickedValue = false
                        cardview.setBackgroundColor(Color.WHITE)
                        clickeditemsList[data.id] = false      //Sparsebooleanarray의 값 변경
                    }
                    else{
                        data.clickedValue = true
                        cardview.setBackgroundColor(Color.GREEN)
                        clickeditemsList[data.id] = true      //Sparsebooleanarray의 값 변경
//                    Log.d("LocationRVAdapter", "locationitems= " + "$locationitems")
//                    Log.d("LocationRVAdapter", "clickeditemsList= " + "$clickeditemsList")
                    }
                }
                else {  //프레그먼트 이동.
                    //
                }
                */
                itemClick(data, cardview, clickeditemsList)//1. 리턴값 : 클릭된 해당 하나짜리 뷰를 객체로 타입 변화하여 통째로 넘겨줌.
            }//


        }
    }   //뷰홀더 함수 끝.

    /*
    fun setLocationItems(locationitems: List<LocationEntity>) { //전체 아이템
        this.locationitems = locationitems
        notifyDataSetChanged()
    }

     */



}


