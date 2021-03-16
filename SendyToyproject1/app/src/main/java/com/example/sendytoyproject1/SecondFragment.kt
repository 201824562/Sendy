package com.example.sendytoyproject1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.containsValue
import androidx.core.util.putAll
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
    var sendingLastBooleanlist = SparseBooleanArray()
    var totalItemCounts = 0

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


        viewModel.getItems().observe(viewLifecycleOwner) {datalist ->

            val adapter = LocationRVAdapter(datalist){
                    it, totalitemcounts, sparsebooleanlist ->
                    //클릭시, 클릭된 그 객체 자체인 it과 그 객체의 위치인 position이 리턴됨. //Toast.makeText(context, "눌러진 아이템 id는 " + it, Toast.LENGTH_SHORT).show()
                    sendingLastBooleanlist = sparsebooleanlist
                    totalItemCounts = totalitemcounts
            }

            recyclerView.adapter = adapter   //뷰에 어댑터 연결
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
           //update UI
           //adapter.setLocationItems(it) //바뀐 리스트를 어댑터에 보내서 리뉴얼해줌.

        }


        deleteButton.setOnClickListener {
            //클릭된 친구들의 리스트를 받아와서 이를 지워주어야한다.
            viewModel.pushSparseArray(sendingLastBooleanlist)
            viewModel.delete()
        }
/*
        allButton.setOnClickListener{
            Log.d("SecondFragment", "눌러졌음요")
            //전체가 선택되었다가 안되었다가하게 해주어야한다.
            if (sendingLastBooleanlist.containsValue(false)){//단 하나라도 false인게 없으면 (즉 모두 선택된 상황=true)
                //1. 모두 색을 빼준다. & 2.클릭된 여부를 모두 없애준다.
                //한꺼번에 어케 색 빼주누..?
                Log.d("SecondFragment", "올 낫 선택")
                sendingLastBooleanlist.clear() //true 다 제거
            }
            else{
                //한꺼번에 어케 색 넣어주누..?
                    for ( i in 0..(totalItemCounts-1)){ //for문이 그냥 눌러진 애가 아니라 전체 아이템 갯수이어야 한다.
                        Log.d("SecondFragment", "올 선택")
                        sendingLastBooleanlist.put(i,true)  //다 true로 만듬
                    }
            }
        }*/
    }

}