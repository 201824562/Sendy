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

        viewModel.alllocations?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var items = viewModel.getItems(it)

            location_rv.adapter = LocationRVAdapter(items, viewModel) {

                Toast.makeText(context, "눌러진 아이템 id는 " + it, Toast.LENGTH_SHORT).show()

                //받아온 SelectedItemslist를 어뜨케 프레그먼트로 전해주지


                var bundle_selected_items = Bundle()
                //val value = it[0]  //번들로 보내줄 값
                //bundle_selected_items.putStringArrayList("item_id", value)
                //findNavController().navigate(레이아웃, bundle_selected_items)
            }
            Log.d("items are :", "!!!!!!!$items!!!!!!")

            location_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        })

        return rootView
    }

}