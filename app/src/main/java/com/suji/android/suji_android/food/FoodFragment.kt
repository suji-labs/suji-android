package com.suji.android.suji_android.food

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.ProductListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodCreateDialogBinding
import com.suji.android.suji_android.databinding.FoodFragmentBinding
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Helper
import com.suji.android.suji_android.listener.ItemClickListener

class FoodFragment : Fragment() {
    private lateinit var binding: FoodFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private lateinit var dialogBinding: FoodCreateDialogBinding
    private val subMenuLayoutID: Int = 0x8000
    private val subMenuNameID: Int = 0x7000
    private val subMenuPriceID: Int = 0x6000
    private var subMenuCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModel()
        binding = DataBindingUtil.inflate<FoodFragmentBinding>(
            inflater,
            R.layout.food_fragment,
            container,
            false)
            .apply {
                adapter = ProductListAdapter(Constant.ViewType.FOOD_VIEW)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                listener = createFood
                mainFoodList.layoutManager = layoutManager
                mainFoodList.adapter = adapter
            }
        dialogBinding = DataBindingUtil.inflate<FoodCreateDialogBinding>(
            inflater,
            R.layout.food_create_dialog,
            null,
            false
        )
        Constant.ListenerHashMap.listenerList["foodDeleteClickListener"] = foodDeleteClickListener
        Constant.ListenerHashMap.listenerList["foodModifyClickListener"] = foodModifyClickListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                foods?.let {
                    adapter.setItems(foods)
                }

                executePendingBindings()
            }
        })
    }

    private var createFood: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                .setPositiveButton("만들기", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val subMenuList: ArrayList<Food> = ArrayList()
                        val foodName: String = dialogBinding.createMenuEditName.text.toString()
                        val foodPrice: String = dialogBinding.createMenuEditPrice.text.toString()

                        dialogBinding.createMenuEditName.setText("")
                        dialogBinding.createMenuEditPrice.setText("")

                        if (Helper.blankString(foodName) || Helper.blankString(foodPrice)) {
                            Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                            return
                        }

                        for (i in 0 until subMenuCount) {
                            val subMenuName =
                                dialogBinding.root.findViewById<BootstrapEditText>(subMenuNameID + i).text.toString()
                            val subMenuPrice =
                                dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString()

                            dialogBinding.root.findViewById<BootstrapEditText>(subMenuNameID + i).setText("")
                            dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i).setText("")

                            subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                        }

                        if (subMenuList.size == 0) {
                            foodViewModel.insert(Food(foodName, foodPrice.toInt()))
                        } else {
                            foodViewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                        }

                        subMenuCount = 0
                        (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                        dialog!!.dismiss()
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        subMenuCount = 0
                        (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                        dialog!!.dismiss()
                    }
                })
                .setNeutralButton("부가 메뉴", null)
                .setView(dialogBinding.root)
                .show()
                .let {
                    it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            addSubMenu()
                        }
                    })
                }

            executePendingBindings()
        }
    }

    private var foodDeleteClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            (item as Food).let {
                foodViewModel.delete(item)
            }

            executePendingBindings()
        }
    }

    private var foodModifyClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            if (item is Food) {
                dialogBinding.createMenuEditName.setText(item.name)
                dialogBinding.createMenuEditPrice.setText(item.price.toString())

                AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                    .setPositiveButton("수정", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val subMenuList: ArrayList<Food> = ArrayList()
                            val foodName: String = dialogBinding.createMenuEditName.text.toString()
                            val foodPrice: String = dialogBinding.createMenuEditPrice.text.toString()

                            if (Helper.blankString(foodName) || Helper.blankString(foodPrice)) {
                                Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                                return
                            }

                            for (i in 0 until subMenuCount) {
                                val subMenuName =
                                    dialogBinding.root.findViewById<BootstrapEditText>(subMenuNameID + i)
                                        .text.toString()
                                val subMenuPrice =
                                    dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i)
                                        .text.toString()
                                subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                            }

                            if (subMenuCount == 0) {
                                foodViewModel.update(Food(foodName, foodPrice.toInt(), id = item.id))
                            } else {
                                foodViewModel.update(Food(foodName, foodPrice.toInt(), subMenuList, item.id))
                            }

                            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                            dialog!!.dismiss()
                        }
                    })
                    .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                            dialog!!.dismiss()
                        }
                    })
                    .setNeutralButton("부가 메뉴", null)
                    .setView(dialogBinding.root)
                    .show()
                    .let {
                        it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                addSubMenu()
                            }
                        })
                    }
            }

            executePendingBindings()
        }
    }

    private fun addSubMenu() {
        val outerLayout: LinearLayout = dialogBinding.createSubMenu
        val innerLayout = LinearLayout(dialogBinding.root.context).apply {
            id = subMenuLayoutID + subMenuCount
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val nameLabel = BootstrapLabel(dialogBinding.root.context).apply {
            text = "이름"
        }

        val nameEditText = BootstrapEditText(dialogBinding.root.context).apply {
            id = subMenuNameID + subMenuCount
            setTextColor(Color.BLACK)
            width = 300
            bottom = 15
        }

        val priceLabel = BootstrapLabel(dialogBinding.root.context).apply {
            text = "가격"
        }

        val priceEditText = BootstrapEditText(dialogBinding.root.context).apply {
            id = subMenuPriceID + subMenuCount
            setTextColor(Color.BLACK)
            inputType = InputType.TYPE_CLASS_NUMBER
            width = 300
            bottom = 15
        }

        val subMenuDelete = BootstrapButton(dialogBinding.root.context).apply {
            id = subMenuCount
            text = "X"
            bootstrapBrand = DefaultBootstrapBrand.DANGER
            setOnClickListener(View.OnClickListener {
                if (subMenuCount < 0) {
                    return@OnClickListener
                }
                val layout = dialogBinding.root.findViewById<LinearLayout>(subMenuLayoutID + it.id)
                outerLayout.removeView(layout)
                subMenuCount--
            })
        }

        innerLayout.addView(nameLabel)
        innerLayout.addView(nameEditText)
        innerLayout.addView(priceLabel)
        innerLayout.addView(priceEditText)
        innerLayout.addView(subMenuDelete)

        outerLayout.addView(innerLayout)
        subMenuCount++
    }

    private fun executePendingBindings() {
        binding.executePendingBindings()
    }
}