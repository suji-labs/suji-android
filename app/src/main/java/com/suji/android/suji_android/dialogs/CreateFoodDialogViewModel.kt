package com.suji.android.suji_android.dialogs

import androidx.lifecycle.MutableLiveData
import com.suji.android.suji_android.base.BaseViewModel

class CreateFoodDialogViewModel : BaseViewModel() {
    val message = MutableLiveData<String>()
    val positiveText = MutableLiveData<String>()
    val neutralText = MutableLiveData<String>()
    val negativeText = MutableLiveData<String>()
    val buttonClicked = MutableLiveData<Int>()

    fun init(message: String, positiveText: String, neutralText: String, negativeText: String) {
        this.message.value = message
        this.positiveText.value = positiveText
        this.neutralText.value = neutralText
        this.negativeText.value = negativeText
    }

    fun onPositive() {
        buttonClicked.value = 0
    }

    fun onNeutral() {
        buttonClicked.value = 1
    }

    fun onNegative() {
        buttonClicked.value = 2
    }
}