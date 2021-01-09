package com.suji.android.suji_android.dialogs

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.base.DataBindingDialogWithVM
import com.suji.android.suji_android.databinding.SujiDialogBinding

class SujiDialog(
    private val message: String,
    private val confirmText: String,
    private val confirmCallback: (() -> Unit)? = null,
    private val dismissText: String,
    private val dismissCallback: (() -> Unit)? = null
) : DataBindingDialogWithVM<SujiDialogBinding, SujiDialogViewModel>(R.layout.suji_dialog, SujiDialogViewModel::class.java) {
    private val viewModel by lazy {
        ViewModelProvider(this).get(SujiDialogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SujiDialog
            vm = viewModel
        }

        viewModel.init(message, confirmText, dismissText)

        initLiveData()
    }

    fun initLiveData() {
        viewModel.buttonClicked.observe(this) {
            if (it) {
                confirmCallback?.invoke()
                dismissAllowingStateLoss()
            } else {
                dismissCallback?.invoke()
                dismissAllowingStateLoss()
            }
        }
    }
}