package com.example.sendytoyproject1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sendytoyproject1.Data.LocationViewmodel
import com.example.sendytoyproject1.databinding.ContentSecondDetailItemBinding
import kotlinx.android.synthetic.main.content_second_detail_item.*

class SecondDetailFragment : Fragment(){
    private val viewmodel : LocationViewmodel by viewModels()
    private lateinit var binding: ContentSecondDetailItemBinding //xml과 바인딩.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(layoutInflater, R.layout.content_second_detail_item,  container, false) //뷰->뷰모델
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeView()
    }

    private fun makeView(){

        val receivedItemdata : LocationItemData? = arguments?.getParcelable("selecteditem_data")
        binding.selectedLocationItemdata = receivedItemdata

        save_memo_btn.setOnClickListener {
            if (receivedItemdata != null) {
                viewmodel.insertmemo(receivedItemdata.id, memoview.text.toString())
            }
        }

    }
}