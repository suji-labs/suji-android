package com.suji.android.suji_android.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.AccountFragmentBinding

class AccountFragment : Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private lateinit var soldItems: List<Sale>
    private var viewModel: AccountViewModel = AccountViewModel(BasicApp.app)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        viewModel.getAllSold().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(t: List<Sale>?) {
                if (t != null) {
                    this@AccountFragment.soldItems = t
                }
            }
        })
    }
}