package com.suji.android.suji_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class DataBindingFragmentWithVM<T : ViewDataBinding, VM : ViewModel>(
    private val layoutId: Int,
    private val viewModelClass: Class<VM>
) : Fragment() {
    protected lateinit var binding: T
    val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }
}