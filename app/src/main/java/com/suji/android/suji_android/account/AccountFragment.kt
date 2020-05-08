package com.suji.android.suji_android.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SoldListAdapter
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.account_fragment.view.*
import java.text.DecimalFormat

class AccountFragment : Fragment() {
    private lateinit var adapter: SoldListAdapter
    private lateinit var items: List<Sale>
    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(this).get(AccountViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()

        val view = inflater.inflate(R.layout.account_fragment, container, false)

        adapter = SoldListAdapter()
        view.sold_fragment_items.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.sold_fragment_items.adapter = adapter

//        view.account_all_btn.setOnClickListener(listener)
//        view.account_day_btn.setOnClickListener(listener)
//        view.account_week_btn.setOnClickListener(listener)
//        view.account_month_btn.setOnClickListener(listener)

        return view
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

        food_sold_total_price.text = DecimalFormat.getCurrencyInstance().format(sumPrice).toString()
        food_sold_card_price.text = DecimalFormat.getCurrencyInstance().format(cardMoney).toString()
        food_sold_cash_price.text = DecimalFormat.getCurrencyInstance().format(cashMoney).toString()
    }

    private fun initView() {
        viewModel.deleteSoldDate(
            Utils.getStartDate(1),
            Utils.getEndDate(1)
        )

        viewModel.getAllSold()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { adapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)
    }

//    private val listener: View.OnClickListener = object : View.OnClickListener {
//        override fun onClick(v: View?) {
//            when (v!!.id) {
//                R.id.account_day_btn -> {
//                    items = viewModel.findSaleOfDate(
//                        Utils.getStartTime(),
//                        Utils.getEndTime()
//                    )
//                    adapter.setItems(items)
//
//                    computePrice(items)
//                }
//                R.id.account_week_btn -> {
//                    items = viewModel.findSaleOfDate(
//                        Utils.getStartWeek(),
//                        Utils.getEndWeek()
//                    )
//                    adapter.setItems(items)
//
//                    computePrice(items)
//                }
//                R.id.account_month_btn -> {
//                    items = viewModel.findSaleOfDate(
//                        Utils.getStartDate(0),
//                        Utils.getEndDate(0)
//                    )
//                    adapter.setItems(items)
//
//                    computePrice(items)
//                }
//                R.id.account_all_btn -> {
//                    viewModel.getAllSold().observe(this@AccountFragment, object : Observer<List<Sale>> {
//                        override fun onChanged(t: List<Sale>?) {
//                            t?.let {
//                                adapter.setItems(t)
//
//                                computePrice(t)
//                            }
//                        }
//                    })
//                }
//            }
//        }
//    }
}