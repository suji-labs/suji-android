package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.sell.SellViewModel

class PayDialogFragment : DialogFragment() {
    private lateinit var builder: AlertDialog.Builder
    private val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this).get(SellViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val item = requireArguments().get("sale") as Sale

        builder = AlertDialog.Builder(activity!!, R.style.AppTheme_AppCompat_CustomDialog)
            .setTitle("결제 방식을 선택하세요")
            .setPositiveButton("현금") { dialogInterface, which ->
                item.isSale = true
                item.pay =
                    Constant.PayType.CASH
                sellViewModel.update(item)
            }
            .setNegativeButton("카드") { dialogInterface, which ->
                item.isSale = true
                item.pay =
                    Constant.PayType.CARD
                sellViewModel.update(item)
            }
            .setNeutralButton("취소") { dialogInterface, which -> dialog!!.dismiss() }
            .setOnCancelListener { (requireView().parent as ViewGroup).removeView(requireView()) }

        return builder.create()
    }

    companion object {
        private val INSTANCE =
            PayDialogFragment()

        fun newInstance(sale: Sale): PayDialogFragment {
            val args = Bundle().apply {
                putParcelable("sale", sale)
            }
            INSTANCE.arguments = args
            return INSTANCE
        }
    }
}