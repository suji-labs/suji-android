package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.base.DataBindingDialogWithVM
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.CreateFoodDialogBinding
import com.suji.android.suji_android.databinding.SubmenuLayoutBinding
import kotlinx.android.synthetic.main.create_food_dialog.view.*
import kotlinx.android.synthetic.main.submenu_layout.view.*

class ModifyFoodDialog(
    private val item: Food,
    private val message: String,
    private val positiveText: String,
    private val positiveCallback: ((CreateFoodDialogBinding) -> Unit)? = null,
    private val neutralText: String,
    private val neutralCallback: ((CreateFoodDialogBinding) -> Unit)? = null,
    private val negativeText: String,
    private val negativeCallback: ((Dialog) -> Unit)? = null
) : DataBindingDialogWithVM<CreateFoodDialogBinding, SujiCustomDialogViewModel>(R.layout.create_food_dialog, SujiCustomDialogViewModel::class.java) {
    private val viewModel: SujiCustomDialogViewModel by lazy {
        ViewModelProvider(this).get(SujiCustomDialogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ModifyFoodDialog
            vm = viewModel
        }

        binding.createMenuEditName.setText(item.name)
        binding.createMenuEditPrice.setText(item.price.toString())

        for (subItem in item.subMenu) {
            val subMenuLayout = DataBindingUtil.inflate<SubmenuLayoutBinding>(LayoutInflater.from(requireContext()), R.layout.submenu_layout, binding.createSubMenu, false)
            subMenuLayout.submenuDelete.setOnClickListener {
                binding.createSubMenu.removeView(subMenuLayout.root)
            }
            subMenuLayout.createSubmenuNameEditText.setText(subItem.name)
            subMenuLayout.createSubmenuPriceEditText.setText(subItem.price.toString())
            binding.createSubMenu.addView(subMenuLayout.root)
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