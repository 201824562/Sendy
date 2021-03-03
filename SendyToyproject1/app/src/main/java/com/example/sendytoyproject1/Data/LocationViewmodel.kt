package com.example.sendytoyproject1.Data

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

//Viewmodel은 구독자. Observe메소드를 포함 O. Observe자체는 하면 X.

class LocationViewmodel(application: Application) : AndroidViewModel(application) {
    //ObservableField 변수 : Observe되는 변수의 값이 변하는지 체크 -> 동적으로 변하게 됨.(관찰만! Bind X)

    var sendedDatetimeText: ObservableField<String> = ObservableField("DateTime")
    var sendedAddressText: ObservableField<String> = ObservableField("Address")

}