package com.suji.android.suji_android.sell

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.dialogs.FoodSalesDialogFragment
import com.suji.android.suji_android.dialogs.PayDialogFragment
import com.suji.android.suji_android.dialogs.SalesModifyDialogFragment
import com.suji.android.suji_android.listener.ItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.sell_fragment.*
import java.time.Instant


class SellFragment : Fragment() {
    private lateinit var adapter: SellListAdapter
    private val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this).get(SellViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()

        return inflater.inflate(R.layout.sell_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SellListAdapter(listener)
        sell_fragment_fab.setOnClickListener(floatingButtonClickListener)
        sell_fragment_items.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sell_fragment_items.adapter = adapter

//        sell_fragment_items.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy < 0) {
//                    sell_fragment_fab.show()
//                } else if (dy > 0) {
//                    sell_fragment_fab.hide()
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                when (newState) {
//                    RecyclerView.SCROLL_STATE_IDLE -> sell_fragment_fab.show()
//                    else -> sell_fragment_fab.hide()
//                }
//            }
//        })
    }

    private fun initView() {
        sellViewModel.getAllSale()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { adapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

    private val floatingButtonClickListener: View.OnClickListener = View.OnClickListener { v ->
            when (v!!.id) {
                R.id.sell_fragment_fab -> {

                    val sale = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        Sale("총 금액", 0, System.currentTimeMillis())
                    } else {
                        Sale("총 금액", 0, Instant.now().toEpochMilli())
                    }

                    FoodSalesDialogFragment.newInstance(sale).show(parentFragmentManager, "FoodSales")
                }
            }
        }

    private val listener = object : ItemClickListener {
        override fun onItemClick(view: View, item: Any?) {
            when (view.id) {
                R.id.sell_item_sold -> {
                    PayDialogFragment.newInstance(item as Sale).show(parentFragmentManager, "dialog")
                }
                R.id.sell_item_modify -> {
                    SalesModifyDialogFragment.newInstance(item as Sale).show(parentFragmentManager, "dialog")
                }
                R.id.sell_item_delete -> {
                    if (item is Sale) {
                        sellViewModel.delete(item)
                    }
                }
            }
        }
    }
}