package com.example.cake.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.cake.R
import com.example.cake.databinding.CakeListFragmentDataBinding
import com.example.cake.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cake_list_fragment.*


internal class CakeListFragment : BaseFragment() {

    private lateinit var binding: CakeListFragmentDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = mainViewModel

        mainViewModel.cakeItemClickedEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { cakeItem ->
                Snackbar.make(
                    binding.cakeListLayout,
                    "You chose ${cakeItem.desc} !!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        binding.refreshBtn.setOnClickListener {
            mainViewModel.getCakeItems()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CakeListFragmentDataBinding
            .inflate(inflater, container, false)

        binding.apply {

            lifecycleOwner = viewLifecycleOwner

            cakeRecyclerView.adapter = Adapter(
                ItemClickListener { cakeItem ->
//                Log.d("Item Clicked", "onCreateView: " + cakeItem.name)
                    mainViewModel.userSelectsItem(cakeItem)
                })
        }

        return binding.root
    }
}