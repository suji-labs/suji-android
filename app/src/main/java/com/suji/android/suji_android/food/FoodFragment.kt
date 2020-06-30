package com.suji.android.suji_android.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.dialogs.CreateFoodDialogFragment
import com.suji.android.suji_android.dialogs.ModifyFoodDialogFragment
import com.suji.android.suji_android.listener.ItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.food_create_dialog.view.*
import kotlinx.android.synthetic.main.food_fragment.view.*

class FoodFragment : Fragment() {
    private lateinit var adapter: FoodListAdapter
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_create_dialog, null, false)
    }
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        val view = inflater.inflate(R.layout.food_fragment, container, false)

        adapter = FoodListAdapter(foodDetail)
        view.main_food_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.main_food_list.adapter = adapter
        view.create_food.setOnClickListener(createFood)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

    private fun initView() {
        foodViewModel.getAllFood()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { adapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)
    }

    private val createFood: View.OnClickListener = View.OnClickListener { v ->
        dialogView.create_menu_edit_name.text.clear()
        dialogView.create_menu_edit_price.text.clear()

        when (v!!.id) {
            R.id.create_food -> {
                CreateFoodDialogFragment.newInstance().show(parentFragmentManager, "dialog")
            }
        }
    }

    private val foodDetail = object : ItemClickListener {
        override fun onItemClick(view: View, item: Any?) {
            when (view.id) {
                R.id.food_modify -> {
                    if (item is Food) {
                        ModifyFoodDialogFragment.newInstance(item).show(parentFragmentManager, "dialog")
                    }
                }
                R.id.food_delete -> {
                    (item as Food).let {
                        foodViewModel.delete(item)
                    }
                }
            }
        }
    }
}