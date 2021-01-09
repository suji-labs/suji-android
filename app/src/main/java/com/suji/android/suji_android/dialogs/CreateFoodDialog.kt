package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.base.DataBindingDialogWithVM
import com.suji.android.suji_android.databinding.CreateFoodDialogBinding
import com.suji.android.suji_android.helper.Log

class CreateFoodDialog(
    private val layoutId: Int,
    private val message: String,
    private val positiveText: String,
    private val positiveCallback: ((CreateFoodDialogBinding) -> Unit)? = null,
    private val neutralText: String,
    private val neutralCallback: ((CreateFoodDialogBinding) -> Unit)? = null,
    private val negativeText: String,
    private val negativeCallback: ((Dialog) -> Unit)? = null
) : DataBindingDialogWithVM<CreateFoodDialogBinding, CreateFoodDialogViewModel>(layoutId, CreateFoodDialogViewModel::class.java) {
    private val viewModel: CreateFoodDialogViewModel by lazy {
        ViewModelProvider(this).get(CreateFoodDialogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@CreateFoodDialog
            vm = viewModel
        }
        lifecycle.addObserver(viewModel)

        viewModel.init(message, positiveText, neutralText, negativeText)

        initLiveData()
    }

    fun initLiveData() {
        viewModel.buttonClicked.observe(this) {
            when (it) {
                0 -> {
                    positiveCallback?.invoke(binding)
                    dismissAllowingStateLoss()
                }
                1 -> { neutralCallback?.invoke(binding) }
                2 -> { negativeCallback?.invoke(requireDialog()) }
            }
        }
    }
}