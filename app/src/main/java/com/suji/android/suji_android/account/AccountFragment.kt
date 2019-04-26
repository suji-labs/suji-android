package com.suji.android.suji_android.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.ProductListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.AccountFragmentBinding
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.listener.ItemClickListener
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import java.text.DecimalFormat


class AccountFragment : Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var viewModel: AccountViewModel = AccountViewModel(BasicApp.app)
    private val dateTime = DateTime()
    private lateinit var items: List<Sale>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModel()
        binding = DataBindingUtil.inflate<AccountFragmentBinding>(
            inflater,
            R.layout.account_fragment,
            container,
            false
        )
            .apply {
                adapter = ProductListAdapter(Constant.ViewType.SOLD_VIEW)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                soldFragmentItems.layoutManager = layoutManager
                soldFragmentItems.adapter = adapter
                adapter.setItems(viewModel.getAllSold())
            }
        binding.day = findDay
        binding.week = findWeek
        binding.month = findMonth
        binding.all = findAll
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun computePrice(items: List<Sale>) {
        var sumPrice = 0
        var cardMoney = 0
        var cashMoney = 0

        for (item in items) {
            sumPrice += item.price

            if (item.pay == Constant.PayType.CARD) {
                cardMoney += item.price
            } else {
                cashMoney += item.price
            }
        }

        binding.foodSoldTotalPrice.text = DecimalFormat.getCurrencyInstance().format(sumPrice).toString()
        binding.foodSoldCardPrice.text = DecimalFormat.getCurrencyInstance().format(cardMoney).toString()
        binding.foodSoldCashPrice.text = DecimalFormat.getCurrencyInstance().format(cashMoney).toString()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
//        viewModel.getAllSold().observe(this, object : Observer<List<Sale>> {
//            override fun onChanged(t: List<Sale>?) {
//                t?.let {
//                    adapter.setItems(t)
//                    soldItems = t
//
//                    computePrice(t)
//                }
//
//                executePendingBindings()
//            }
//        })
    }

    private val findDay: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            items = viewModel.findSaleOfDate(
                dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0),
                dateTime.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
            )
            adapter.setItems(items)

            computePrice(items)
        }
    }

    private val findWeek: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            items = viewModel.findSaleOfDate(
                dateTime.withDayOfWeek(DateTimeConstants.MONDAY),
                dateTime.withDayOfWeek(DateTimeConstants.SUNDAY)
            )
            adapter.setItems(items)

            computePrice(items)
        }
    }

    private val findMonth: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            val days = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

            if (dateTime.withYear(dateTime.year).year().isLeap) {
                days[1] = 29
            } else {
                days[1] = 28
            }

            items = viewModel.findSaleOfDate(
                dateTime
                    .withMonthOfYear(dateTime.monthOfYear)
                    .withDayOfMonth(1)
                    .withHourOfDay(0)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0),
                dateTime
                    .withMonthOfYear(dateTime.monthOfYear)
                    .withDayOfMonth(days[dateTime.monthOfYear - 1])
                    .withHourOfDay(23)
                    .withMinuteOfHour(59)
                    .withSecondOfMinute(59)
            )
            adapter.setItems(items)

            computePrice(items)
        }
    }

    private val findAll: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            adapter.setItems(viewModel.getAllSold())
            computePrice(viewModel.getAllSold())
//            viewModel.getAllSold().observe(this@AccountFragment, object : Observer<List<Sale>> {
//                override fun onChanged(t: List<Sale>?) {
//                    t?.let {
//                        adapter.setItems(t)
//                        soldItems = t
//
//                        computePrice(t)
//                    }
//
//                    executePendingBindings()
//                }
//            })
        }
    }

    private fun executePendingBindings() {
        binding.executePendingBindings()
    }
}