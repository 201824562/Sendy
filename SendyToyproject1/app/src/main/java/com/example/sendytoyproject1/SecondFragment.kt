package com.example.sendytoyproject1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationViewmodel

class SecondFragment: Fragment()  {

    private val viewModel: LocationViewmodel by activityViewModels() //뷰모델을 사용하기 위해 접근

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_second, container, false)
        //val binding: FragmentFirstBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_first,  container, false)
        //val rootView : View = binding.root


        var location_rv = rootView.findViewById(R.id.recyclerView) as RecyclerView
        val adapter = LocationRVAdapter(){
            

            Toast.makeText(context, "눌러진 아이템 id는 " + it, Toast.LENGTH_SHORT).show()


            //var bundle_selected_items = Bundle()
            //val value = it[0]  //번들로 보내줄 값
            //bundle_selected_items.putStringArrayList("item_id", value)
            //findNavController().navigate(레이아웃, bundle_selected_items)
        }

        location_rv.adapter = adapter
        location_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        // getItems(), 즉 전체 아이템 리스트에 변화가 생기는 경우를 가져와준다.(관찰함)
        viewModel.getItems()?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
             //update UI
            adapter.setLocationItems(it) //바뀐 리스트를 어댑터에 보내서 리뉴얼해줌.
        })


        return rootView
    }

}