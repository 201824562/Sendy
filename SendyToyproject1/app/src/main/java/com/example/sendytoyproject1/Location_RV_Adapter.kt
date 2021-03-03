package com.example.sendytoyproject1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.location_item.view.*

//뷰홀더에 대한 정확한 이해도 공부 필요.
class Location_RV_Adapter(Location_Data : MutableList<Location_Item_Data>): RecyclerView.Adapter<Location_RV_Adapter.SearchViewHolder>()  {
    var items: MutableList<Location_Item_Data> = Location_Data //매개변수 형식으로 받아온 데이터를 똑같은 형식으로 내부서 재선언.

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =SearchViewHolder(parent)

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) { //customizing한 뷰 홀더에 데이터를 Binding해준다.
        items[position].let { item -> //5.3.7.
            with(holder) {
                DateTime.text = item.datetime
                Location.text = item.location
                //itemView.setOnClickListener{ itemClick(item) } //Mutable X, Tea_Data 클릭리스너 생성.
            }
        }
    }
    override fun getItemCount(): Int = items.size

    inner class SearchViewHolder (parent: ViewGroup) : RecyclerView.ViewHolder( //location_item.xml에 연동시켜서 뷰홀더를 customizing하였음.
        LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)) {
        val DateTime = itemView.Datetime_text
        val Location = itemView.locationinfo_text
    }

}


