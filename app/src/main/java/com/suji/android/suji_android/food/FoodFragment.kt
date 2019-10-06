package com.suji.android.suji_android.food

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.ProductListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import kotlinx.android.synthetic.main.food_create_dialog.view.*
import kotlinx.android.synthetic.main.food_fragment.view.*
import kotlinx.android.synthetic.main.sub_menu_layout.view.*

class FoodFragment : Fragment() {
    private lateinit var adapter: ProductListAdapter
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProviders.of(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_create_dialog, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModel()
        val view = inflater.inflate(R.layout.food_fragment, container, false)

        adapter = ProductListAdapter(Constant.ViewType.FOOD_VIEW)
        view.main_food_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.main_food_list.adapter = adapter
        view.create_food.setOnClickListener(createFood)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                foods?.let {
                    adapter.setItems(foods)
                }
            }
        })
    }

    private val createFood: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.create_food -> {
                    AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                        .setPositiveButton("만들기", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                val subMenuList: ArrayList<Food> = ArrayList()
                                val foodName: String = dialogView.create_menu_edit_name.text.toString()
                                val foodPrice: String = dialogView.create_menu_edit_price.text.toString()

                                dialogView.create_menu_edit_name.text.clear()
                                dialogView.create_menu_edit_price.text.clear()

                                if (Utils.blankString(foodName) || Utils.blankString(foodPrice)) {
                                    Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                                    return
                                }

                                for (i in 0 until dialogView.create_sub_menu.childCount) {
                                    val subMenuView = dialogView.create_sub_menu.getChildAt(i)
                                    val subMenuName = subMenuView.sub_menu_name_edit_text.text.toString()
                                    val subMenuPrice = subMenuView.sub_menu_price_edit_text.text.toString()

                                    subMenuView.sub_menu_name_edit_text.text.clear()
                                    subMenuView.sub_menu_price_edit_text.text.clear()

                                    subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                                }

                                if (subMenuList.size == 0) {
                                    foodViewModel.insert(Food(foodName, foodPrice.toInt()))
                                } else {
                                    foodViewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                                }

                                (dialogView.parent as ViewGroup).removeView(dialogView)
                                dialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                (dialogView.parent as ViewGroup).removeView(dialogView)
                                dialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("부가 메뉴", null)
                        .setView(dialogView)
                        .show()
                        .let {
                            it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    val subMenuLayout = layoutInflater.inflate(R.layout.sub_menu_layout, null)
                                    subMenuLayout.sub_menu_delete.setOnClickListener {
                                        dialogView.create_sub_menu.removeView(subMenuLayout)
                                    }
                                    dialogView.create_sub_menu.addView(subMenuLayout)
                                }
                            })

                            Utils.dialogReSizing(it)
                        }

                }
            }
        }
    }

//    private var foodDeleteClickListener: ItemClickListener = object : ItemClickListener {
//        override fun onClick(item: Any?) {
//            (item as Food).let {
//                foodViewModel.delete(item)
//            }
//
////            executePendingBindings()
//        }
//    }
//
//    private var foodModifyClickListener: ItemClickListener = object : ItemClickListener {
//        override fun onClick(item: Any?) {
//            if (item is Food) {
//                dialogView.createMenuEditName.setText(item.name)
//                dialogView.createMenuEditPrice.setText(item.price.toString())
//
//                AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
//                    .setPositiveButton("수정", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            val subMenuList: ArrayList<Food> = ArrayList()
//                            val foodName: String = dialogView.createMenuEditName.text.toString()
//                            val foodPrice: String = dialogView.createMenuEditPrice.text.toString()
//
//                            if (Utils.blankString(foodName) || Utils.blankString(foodPrice)) {
//                                Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
//                                return
//                            }
//
//                            for (i in 0 until subMenuCount) {
//                                val subMenuName =
//                                    dialogView.root.findViewById<BootstrapEditText>(subMenuNameID + i)
//                                        .text.toString()
//                                val subMenuPrice =
//                                    dialogView.root.findViewById<BootstrapEditText>(subMenuPriceID + i)
//                                        .text.toString()
//                                subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
//                            }
//
//                            if (subMenuCount == 0) {
//                                foodViewModel.update(Food(foodName, foodPrice.toInt(), id = item.id))
//                            } else {
//                                foodViewModel.update(Food(foodName, foodPrice.toInt(), subMenuList, item.id))
//                            }
//
//                            (dialogView.root.parent as ViewGroup).removeView(dialogView.root)
//                            dialog!!.dismiss()
//                        }
//                    })
//                    .setNegativeButton("취소", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            (dialogView.root.parent as ViewGroup).removeView(dialogView.root)
//                            dialog!!.dismiss()
//                        }
//                    })
//                    .setNeutralButton("부가 메뉴", null)
//                    .setView(dialogView.root)
//                    .show()
//                    .let {
//                        it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
//                            override fun onClick(v: View?) {
//                                addSubMenu()
//                            }
//                        })
//
//                        Utils.dialogReSizing(it)
//                    }
//            }
//
//        }
//    }
}