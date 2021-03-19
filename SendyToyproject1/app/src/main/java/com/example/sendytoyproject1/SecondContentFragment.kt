package com.example.sendytoyproject1

import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.set
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.ContentSecondHomeBinding
import kotlinx.android.synthetic.main.content_second_home.*


class SecondContentFragment: Fragment() {

    private val viewModel: LocationViewmodel by viewModels() //뷰모델을 사용하기 위해 접근
    //private lateinit var binding: ContentSecondHomeBinding
    var clickedstate: Boolean = false
    var clickeditemsList = SparseBooleanArray()     //이거 관련해서 어댑터 정리하기



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val rootView = inflater.inflate(R.layout.fragment_second, container, false)

/*
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.content_second_home,
            container,
            false
        ) //뷰->뷰모델 */
        //val rootView: View = binding.root
        val rootView = inflater.inflate(R.layout.content_second_home, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.lifecycleOwner = this
        //binding.viewModel = viewModel

        viewModel.getItems().observe(viewLifecycleOwner) { datalist ->

            val adapter = LocationRVAdapter(datalist) { itemdata, itemview, sendingLastBooleanlist ->
                //클릭시, 클릭된 그 객체 자체인 it과 그 객체의 위치인 position이 리턴됨. //Toast.makeText(context, "눌러진 아이템 id는 " + it, Toast.LENGTH_SHORT).show()

                //클릭된 아이템의 경우 뷰의 배경색을 바꾸게 설정하는 코드.
                if (clickedstate == true) { //선택이 되는 모드일 경우
                    if (itemdata.clickedValue) { //true일 경우
                        itemdata.clickedValue = false
                        itemview.setBackgroundColor(Color.WHITE)
                        clickeditemsList[itemdata.id] = false      //Sparsebooleanarray의 값 변경
                    } else {
                        itemdata.clickedValue = true
                        itemview.setBackgroundColor(Color.GREEN)
                        clickeditemsList[itemdata.id] = true      //Sparsebooleanarray의 값 변경

                    }
                } else {  //프레그먼트 이동.
                    var bundle_selectedItem = Bundle();
                    bundle_selectedItem.putParcelable("selecteditem_data", itemdata)

                    findNavController().navigate(R.id.action_secondFragment_to_secondDetailFragment, bundle_selectedItem)
                }
            }

            recyclerView.adapter = adapter   //뷰에 어댑터 연결
            recyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            //update UI
            //adapter.setLocationItems(it) //바뀐 리스트를 어댑터에 보내서 리뉴얼해줌.

        }

        clickButton.setOnClickListener {
            clickedstate = (!clickedstate)
            if (clickedstate == true) { //선택X -> 선택O
                clickButton.setBackgroundColor(Color.GREEN)
            } else {
                clickButton.setBackgroundColor(Color.GRAY)
                //수정 필요하다.
            }

            deleteButton.setOnClickListener {
                //클릭된 친구들의 리스트를 받아와서 이를 지워주어야한다.
                if (clickedstate == true) { //만약 선택가능한 모드일 경우
                    if (clickeditemsList == null) { //선택된 아이가 없을 경우
                        Toast.makeText(context, "삭제할 아이템을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    } else viewModel.delete(clickeditemsList)
                } else {
                    Toast.makeText(context, "선택모드로 하셔야합니다.", Toast.LENGTH_SHORT)
                        .show() //이때는 삭제가 안되어요.
                }
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
}