package com.example.sendytoyproject1

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationEntity
import com.example.sendytoyproject1.Data.LocationViewmodel
import kotlinx.android.synthetic.main.location_item.view.*

//뷰홀더에 대한 정확한 이해도 공부 필요.
class LocationRVAdapter(var itemClick: (LocationEntity) -> Unit): RecyclerView.Adapter<LocationRVAdapter.SearchViewHolder>()  {


    var locationitems: List<LocationEntity> = listOf() //매개변수 형식으로 받아온 데이터를 똑같은 형식으로 내부서 재선언.
    var SelectedItemslist = SparseBooleanArray() //총 눌러진 아이템들 표현하는 리스트 -> livedata이어야 하나?



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(parent)

    override fun getItemCount(): Int = locationitems.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) { //customizing한 뷰 홀더에 데이터를 Binding해준다.
        holder.bind(locationitems[position]) //LocationEntity list에서 하나 보내줌.

    }

    //뷰 홀더 -> 뷰 하나짜리.
    inner class SearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder( //location_item.xml에 연동시켜서 뷰홀더를 customizing하였음.
            LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false))
    {
        //location_item -> itemView / xml의 뷰 이름 가져옴.
        val DateTime = itemView.Datetime_text
        val Location = itemView.locationinfo_text
        val Cardview = itemView.cardview

        fun bind(locationitem : LocationEntity){ //(커스텀) 하나씩 바인딩 해줌. -> 뷰 하나==아이템 '하나'
            DateTime.text = locationitem.datetime
            Location.text = locationitem.address


            //뷰 "개인"의 클릭리스너
            itemView.setOnClickListener{
                itemClick(locationitem)//클릭된 해당 하나짜리 뷰를 객체로 타입 변화하여 통째로 넘겨줌.

                //클릭된 아이템의 경우 뷰의 배경색을 바꾸게 설정하는 코드.
                if (locationitem.clickedValue){ //true일 경우
                    locationitem.clickedValue = false
                    Cardview.setBackgroundColor(Color.WHITE)
                }
                else{
                    locationitem.clickedValue = true
                    Cardview.setBackgroundColor(Color.GREEN)
                }
            }//


        }
    }   //뷰홀더 함수 끝.


    fun setLocationItems(locationitems: List<LocationEntity>) { //전체 아이템
        this.locationitems = locationitems
        notifyDataSetChanged()
    }
}


