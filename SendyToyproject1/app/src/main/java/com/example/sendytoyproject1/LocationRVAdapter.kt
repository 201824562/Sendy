package com.example.sendytoyproject1

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationViewmodel
import kotlinx.android.synthetic.main.location_item.view.*

//뷰홀더에 대한 정확한 이해도 공부 필요.
class LocationRVAdapter(Location_Data: MutableList<Location_Item_Data>, var viewmodel: LocationViewmodel , var itemClick: (Location_Item_Data) -> Unit): RecyclerView.Adapter<LocationRVAdapter.SearchViewHolder>()  {


    var items: MutableList<Location_Item_Data> = Location_Data //매개변수 형식으로 받아온 데이터를 똑같은 형식으로 내부서 재선언.
    var SelectedItemslist = SparseBooleanArray() //총 눌러진 아이템들 표현하는 리스트 -> livedata이어야 하나?



     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =SearchViewHolder(parent, itemClick)

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) { //customizing한 뷰 홀더에 데이터를 Binding해준다.
        items[position].let { item -> //5.3.7.
            with(holder) {
                DateTime.text = item.datetime
                Location.text = item.location

                //viewmodel.delete()

                itemView.setOnClickListener{
                    itemClick(item)//해당 position인 'Lovation_Item_Data'를 객체로 타입 변화하여  통째로 넘겨줌.

                    //클릭된 아이템의 경우 뷰의 배경색을 바꾸게 설정하는 코드.
                    if (SelectedItemslist.get(position, false)){
                        SelectedItemslist.put(position, false)
                        Cardview.setBackgroundColor(Color.WHITE)

                    }
                    else{
                        SelectedItemslist.put(position, true)
                        Cardview.setBackgroundColor(Color.GREEN)
                    }


                } //Mutable X, Location_Item_Data 클릭리스너 생성.

            }
        }
    }

    override fun getItemCount(): Int = items.size


    inner class SearchViewHolder(parent: ViewGroup, itemClick: (Location_Item_Data) -> Unit) : RecyclerView.ViewHolder( //location_item.xml에 연동시켜서 뷰홀더를 customizing하였음.
            LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)) {
        //location_item -> itemView / xml의 뷰 이름 가져옴.
        val DateTime = itemView.Datetime_text
        val Location = itemView.locationinfo_text
        val Cardview = itemView.cardview
    }

}


