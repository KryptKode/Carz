package com.kryptkode.carz.ui.manufacturer

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kryptkode.carz.R
import com.kryptkode.carz.databinding.FragmentManufacturerBinding
import com.kryptkode.carz.navigator.Navigator
import com.kryptkode.carz.utils.idling.IAppIdlingResource
import com.kryptkode.carz.utils.pagination.EndlessRecyclerViewScrollListener
import com.kryptkode.carz.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ManufacturerFragment : Fragment(R.layout.fragment_manufacturer) {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var idlingResource: IAppIdlingResource

    private val binding by viewBinding(FragmentManufacturerBinding::bind)

    private val viewModel by viewModels<ManufacturerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ManufacturerAdapter { manufacturer ->
            navigator.toCarType(manufacturer)
        }

        binding.retryButton.setOnClickListener {
            viewModel.refreshData()
        }

        binding.bottomRetryButton.setOnClickListener {
            viewModel.refreshData()
        }

        binding.appBarLayout.toolbar.title = getString(R.string.title_manufacturer)
        binding.recyclerView.adapter = adapter

        idlingResource.setIdleState(false)
        adapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    idlingResource.setIdleState(true)
                }
            }
        )

        val scrollListener = object : EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.getManufacturers(page)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAllData()
            scrollListener.resetState()
        }

        binding.recyclerView.addOnScrollListener(scrollListener)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        viewModel.viewState.onEach {
            Timber.d("STATE: $it")
            binding.swipeRefresh.isRefreshing = it.loading

            binding.errorGroup.isVisible = it.error && it.empty
            binding.errorTextView.text = it.errorMessage
            binding.loadingGroup.isVisible = it.loading && it.empty

            binding.bottomErrorGroup.isVisible = it.error && it.empty.not()
            binding.bottomErrorTextView.text = it.errorMessage
            binding.bottomLoadingTextView.isVisible = it.loading && it.empty.not()

            adapter.submitList(it.manufacturers)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
