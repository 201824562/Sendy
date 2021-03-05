package com.example.sendytoyproject1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.FragmentFirstBinding

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

        //임의의 데이터 -> 후에 제대로 구현 필요.
        var items: MutableList<Location_Item_Data> = mutableListOf(
        Location_Item_Data("2021-03-02", "부산 진구"), Location_Item_Data("2021-03-03", "부산 남구")

         )

        viewModel.alllocations?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var items = viewModel.getItems(it)

            location_rv.adapter =Location_RV_Adapter(items)
            Log.d("items are :", "!!!!!!!$items!!!!!!")

            //클릭리스너 구현시 필요
            /*{
            var bundle_selectedTea = Bundle();
            bundle_selectedTea.putString("tea_name", Tea_Item_Data.teaname);
            findNavController().navigate(R.id.action_secondHomeScreen_to_teaInfoFragment, bundle_selectedTea)
            }*/

            location_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        })

        return rootView
    }

}