package com.suji.android.suji_android.account

import android.os.Bundle
import android.view.View
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SoldListAdapter
import com.suji.android.suji_android.base.DataBindingFragmentWithVM
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.AccountFragmentBinding
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.account_fragment.view.*
import java.text.DecimalFormat

class AccountFragment : DataBindingFragmentWithVM<AccountFragmentBinding, AccountViewModel>(R.layout.account_fragment, AccountViewModel::class.java) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@AccountFragment
            vm = viewModel
            handler = ClickHandler()
            soldFragmentItems.adapter = SoldListAdapter(viewModel)
        }
        lifecycle.addObserver(viewModel)

        viewModel.deleteSoldDate(
            Utils.getStartDate(1),
            Utils.getEndDate(1)
        )

        initLiveData()
    }

    private fun computePrice(items: List<Sale>) {
        binding.foodSoldTotalPrice.text  = DecimalFormat.getCurrencyInstance().format(viewModel.computePrice(items, Constant.PayType.ALL)).toString()
        binding.foodSoldCardPrice.text = DecimalFormat.getCurrencyInstance().format(viewModel.computePrice(items, Constant.PayType.CARD)).toString()
        binding.foodSoldCashPrice.text = DecimalFormat.getCurrencyInstance().format(viewModel.computePrice(items, Constant.PayType.CASH)).toString()
    }

    private fun initLiveData() {
        viewModel.soldList.observe(this) {
            binding.soldFragmentItems.adapter?.notifyDataSetChanged()
        }
    }

    inner class ClickHandler {
        fun dayClicked() {
            viewModel.findSoldDate(
                Utils.getStartTime(),
                Utils.getEndTime()
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewModel.soldList.value = it
                        computePrice(it)
                    },
                    { e -> e.printStackTrace() }
                ).addTo(viewModel.compositeDisposable)
        }

        fun weekClicked() {
            viewModel.findSoldDate(
                Utils.getStartWeek(),
                Utils.getEndWeek()
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewModel.soldList.value = it
                        computePrice(it)
                    },
                    { e -> e.printStackTrace() }
                ).addTo(viewModel.compositeDisposable)
        }

        fun monthClicked() {
            viewModel.findSoldDate(
                Utils.getStartDate(0),
                Utils.getEndDate(0)
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewModel.soldList.value = it
                        computePrice(it)
                    },
                    { e -> e.printStackTrace() }
                ).addTo(viewModel.compositeDisposable)
        }

        fun allClicked() {
            viewModel.getAllSold()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewModel.soldList.value = it
                        computePrice(it)
                    },
                    { e -> e.printStackTrace() }
                ).addTo(viewModel.compositeDisposable)
        }
    }
}