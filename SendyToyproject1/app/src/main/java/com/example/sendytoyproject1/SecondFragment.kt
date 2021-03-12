package com.example.sendytoyproject1

import android.content.ContentValues.TAG
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.FragmentFirstBinding
import com.example.sendytoyproject1.databinding.FragmentSecondBinding
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment: Fragment()  {

    private val viewModel: LocationViewmodel by viewModels() //뷰모델을 사용하기 위해 접근
    private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val rootView = inflater.inflate(R.layout.fragment_second, container, false)
        binding  = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_second,  container, false) //뷰->뷰모델
        val rootView : View = binding.root
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        //binding.viewModel = viewModel


        //어댑터로 인해 변하는 값이 없으므로!
        val items = viewModel.getItems()
        Log.d(TAG, "가진 거" + items.toString())

        viewModel.alllocations.observe(viewLifecycleOwner) {
            Log.d("SecondFragment", "$it")

            val adapter = LocationRVAdapter(it){
               Toast.makeText(context, "눌러진 아이템 id는 " + it, Toast.LENGTH_SHORT).show()
                //var bundle_selected_items = Bundle()
                //val value = it[0]  //번들로 보내줄 값
               //bundle_selected_items.putStringArrayList("item_id", value)
                //findNavController().navigate(레이아웃, bundle_selected_items)
            }

            recyclerView.adapter = adapter   //뷰에 어댑터 연결
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
           //update UI
           //adapter.setLocationItems(it) //바뀐 리스트를 어댑터에 보내서 리뉴얼해줌.

        }






    }
}