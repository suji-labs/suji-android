package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.base.DataBindingDialogWithVM
import com.suji.android.suji_android.databinding.CreateFoodDialogBinding
import com.suji.android.suji_android.databinding.FoodSellDialogBinding

class SujiCustomDialog<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val message: String,
    private val positiveText: String,
    private val positiveCallback: ((T) -> Unit)? = null,
    private val neutralText: String,
    private val neutralCallback: ((T) -> Unit)? = null,
    private val negativeText: String,
    private val negativeCallback: ((Dialog) -> Unit)? = null
) : DataBindingDialogWithVM<T, SujiCustomDialogViewModel>(layoutId, SujiCustomDialogViewModel::class.java) {
    private val viewModel: SujiCustomDialogViewModel by lazy {
        ViewModelProvider(this).get(SujiCustomDialogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (binding) {
            is CreateFoodDialogBinding -> {
                (binding as? CreateFoodDialogBinding)?.apply {
                    lifecycleOwner = this@SujiCustomDialog
                    vm = viewModel
                }
            }
        }
//        binding.apply {
//            lifecycleOwner = this@SujiCustomDialog
//            vm = viewModel
//        }
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