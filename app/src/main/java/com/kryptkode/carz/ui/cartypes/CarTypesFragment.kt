package com.kryptkode.carz.ui.cartypes

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.databinding.FragmentCarTypeBinding
import com.kryptkode.carz.navigator.Navigator
import com.kryptkode.carz.utils.pagination.EndlessRecyclerViewScrollListener
import com.kryptkode.carz.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class CarTypesFragment : Fragment(R.layout.fragment_car_type) {

    @Inject
    lateinit var navigator: Navigator

    private val binding by viewBinding(FragmentCarTypeBinding::bind)
    private val viewModel by viewModels<CarTypesViewModel>()
    private val manufacturer by lazy { arguments?.getParcelable<CarManufacturer>(MANUFACTURER_KEY)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChip()
        setUpToolbar()

        val adapter = CarTypesAdapter {
            navigator.toCarBuildDate(manufacturer, it)
        }

        binding.retryButton.setOnClickListener {
            viewModel.refreshData()
        }

        binding.bottomRetryButton.setOnClickListener {
            viewModel.refreshData()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        binding.searchEditText.addTextChangedListener {
            viewModel.searchCarTypes(manufacturer.id, query = it.toString())
        }

        binding.recyclerView.adapter = adapter
        val scrollListener = object : EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.getCarTypes(page)
            }
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

            adapter.submitList(it.types)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.searchCarTypes(manufacturer.id)
    }

    private fun setUpToolbar() {
        binding.appBarLayout.toolbar.title = getString(R.string.title_car_type)
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            navigator.navigateUp()
        }
    }

    private fun setUpChip() {
        val chip = Chip(requireContext())
        chip.text = manufacturer.name
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            navigator.navigateUp()
        }
        binding.chipGroup.addView(chip)
    }

    companion object {
        private const val MANUFACTURER_KEY = "manufacturer"

        fun buildArguments(manufacturer: CarManufacturer): Bundle {
            return bundleOf(MANUFACTURER_KEY to manufacturer)
        }
    }
}
