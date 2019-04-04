package com.suji.android.suji_android.food

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
import com.suji.android.suji_android.adapter.FoodListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodCreateDialogBinding
import com.suji.android.suji_android.databinding.FoodFragmentBinding
import com.suji.android.suji_android.helper.DialogHelper
import com.suji.android.suji_android.listener.CreateFoodClickListener
import com.suji.android.suji_android.listener.FoodClickListener

class FoodFragment : Fragment() {
    private lateinit var binding: FoodFragmentBinding
    private lateinit var adapter: FoodListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FoodFragmentBinding>(inflater, R.layout.food_fragment, container, false)
        initViewModel()
        adapter = FoodListAdapter(foodClickListener)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.listener = createFood
        binding.mainFoodList.layoutManager = layoutManager
        binding.mainFoodList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                if (foods != null) {
                    adapter.setFoodList(foods)
                }

                binding.executePendingBindings()
            }
        })
    }

    private var createFood: CreateFoodClickListener = object : CreateFoodClickListener {
        val subMenuLayoutID: Int = 0x8000
        val subMenuNameID: Int = 0x7000
        val subMenuPriceID: Int = 0x6000
        var subMenuCount = 0

        override fun onClick() {
            DialogHelper.Builder(context!!, R.layout.food_create_dialog)
                .setPositiveButton("만들기", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val inflater = LayoutInflater.from(context)
                        val view = inflater.inflate(R.layout.food_create_dialog, null)
                        val createMenuBinding = DataBindingUtil.inflate<FoodCreateDialogBinding>(
                            inflater,
                            R.layout.food_create_dialog,
                            null,
                            false
                        )

                        val subMenuList: ArrayList<Food> = ArrayList()
                        val foodName: String = createMenuBinding.createMenuEditName.text.toString()
                        val foodPrice: String = createMenuBinding.createMenuEditPrice.text.toString()

                        if (foodName == "" || foodPrice == "") {
                            Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                            return
                        }

                        for (i in 0 until subMenuCount) {
                            val subMenuName = view.findViewById<BootstrapEditText>(subMenuNameID + i).text.toString()
                            val subMenuPrice = view.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString()
                            subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                        }

                        if (subMenuList.size == 0) {
                            foodViewModel.insert(Food(foodName, foodPrice.toInt()))
                        } else {
                            foodViewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                        }

                        dialog!!.dismiss()
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.dismiss()
                    }
                })
                .setNegativeButton("부가 메뉴", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val inflater = LayoutInflater.from(context)
                        val view = inflater.inflate(R.layout.food_create_dialog, null)
                        val createMenuBinding = DataBindingUtil.inflate<FoodCreateDialogBinding>(
                            inflater,
                            R.layout.food_create_dialog,
                            null,
                            false
                        )

                        val outerLayout: LinearLayout = view.findViewById(R.id.create_sub_menu)
                        val innerLayout = LinearLayout(context)
                        innerLayout.id = subMenuLayoutID + subMenuCount
                        innerLayout.orientation = LinearLayout.HORIZONTAL
                        innerLayout.gravity = Gravity.CENTER

                        val nameLabel = BootstrapLabel(context)
                        nameLabel.text = "부가 메뉴 이름"

                        val nameEditText = BootstrapEditText(context)
                        nameEditText.id = subMenuNameID + subMenuCount
                        nameEditText.setTextColor(Color.BLACK)
                        nameEditText.width = 150
                        nameEditText.bottom = 15

                        val priceLabel = BootstrapLabel(context)
                        priceLabel.text = "부가 메뉴 가격"

                        val priceEditText = BootstrapEditText(context)
                        priceEditText.id = subMenuPriceID + subMenuCount
                        priceEditText.setTextColor(Color.BLACK)
                        priceEditText.inputType = InputType.TYPE_CLASS_NUMBER
                        priceEditText.width = 150
                        priceEditText.bottom = 15

                        val subMenuDelete = BootstrapButton(context)
                        subMenuDelete.id = subMenuCount
                        subMenuDelete.text = "X"
                        subMenuDelete.bootstrapBrand = DefaultBootstrapBrand.DANGER
                        subMenuDelete.setOnClickListener(View.OnClickListener {
                            if (subMenuCount < 0) {
                                return@OnClickListener
                            }
                            val layout = view.findViewById<LinearLayout>(subMenuLayoutID + it.id)
                            outerLayout.removeView(layout)
                            subMenuCount--
                        })

                        innerLayout.addView(nameLabel)
                        innerLayout.addView(nameEditText)
                        innerLayout.addView(priceLabel)
                        innerLayout.addView(priceEditText)
                        innerLayout.addView(subMenuDelete)

                        outerLayout.addView(innerLayout)
                        subMenuCount++
                    }
                })
                .build()
                .show()
        }
    }

    private var foodClickListener: FoodClickListener = object : FoodClickListener {
        override fun onDeleteClick(food: Food) {
            foodViewModel.delete(food)
        }

        override fun onModifyClick(food: Food) {
//            DialogHelper(context!!, R.layout.food_create_dialog, food, foodViewModel).show()
        }
    }
}