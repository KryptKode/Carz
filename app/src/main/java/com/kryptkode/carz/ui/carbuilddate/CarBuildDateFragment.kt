package com.kryptkode.carz.ui.carbuilddate

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.databinding.FragmentCarBuildDateBinding
import com.kryptkode.carz.navigator.Navigator
import com.kryptkode.carz.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CarBuildDateFragment : Fragment(R.layout.fragment_car_build_date) {
    private val binding by viewBinding(FragmentCarBuildDateBinding::bind)
    private val viewModel by viewModels<CarBuildDateViewModel>()
    private val manufacturer by lazy { arguments?.getParcelable<CarManufacturer>(MANUFACTURER_KEY)!! }
    private val carType by lazy { arguments?.getParcelable<CarType>(CAR_TYPE_KEY)!! }

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChip()
        setUpToolbar()

        val adapter = CarBuildDateAdapter { buildDate ->
            navigator.toSummary(manufacturer, carType, buildDate)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        viewModel.viewState.onEach {
            binding.recyclerView.adapter = adapter
            binding.swipeRefresh.isEnabled = it.loading
            binding.swipeRefresh.isRefreshing = it.loading
            binding.errorGroup.isVisible = it.error || it.loading
            binding.retryButton.isVisible = it.error
            binding.errorImage.setImageResource(if (it.error) R.drawable.ic_cloud else R.drawable.ic_date)
            binding.errorTextView.text =
                if (it.error) it.errorMessage else getString(R.string.build_dates_loading_message)

            adapter.submitList(it.buildDates)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getCarBuildDates(manufacturer.id, carType.id)
    }

    private fun setUpToolbar() {
        binding.appBarLayout.toolbar.title = getString(R.string.title_build_date)
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            navigator.navigateUp()
        }
    }

    private fun setUpChip() {
        val manufacturerChip = Chip(requireContext())
        manufacturerChip.text = manufacturer.name
        manufacturerChip.isCloseIconVisible = true
        manufacturerChip.setOnCloseIconClickListener {
            navigator.fromCarBuildDateToManufacturer()
        }

        val carTypeChip = Chip(requireContext())
        carTypeChip.text = carType.name
        carTypeChip.isCloseIconVisible = true
        carTypeChip.setOnCloseIconClickListener {
            navigator.navigateUp()
        }
        binding.chipGroup.addView(manufacturerChip)
        binding.chipGroup.addView(carTypeChip)
    }

    companion object {
        private const val MANUFACTURER_KEY = "manufacturer"
        private const val CAR_TYPE_KEY = "car_type"

        fun buildArguments(manufacturer: CarManufacturer, carType: CarType): Bundle {
            return bundleOf(
                MANUFACTURER_KEY to manufacturer,
                CAR_TYPE_KEY to carType
            )
        }
    }
}
