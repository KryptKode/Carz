package com.kryptkode.carz.ui.summary

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.databinding.FragmentSummaryBinding
import com.kryptkode.carz.navigator.Navigator
import com.kryptkode.carz.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SummaryFragment : Fragment(R.layout.fragment_summary) {

    @Inject
    lateinit var navigator: Navigator

    private val binding by viewBinding(FragmentSummaryBinding::bind)
    private val manufacturer by lazy { arguments?.getParcelable<CarManufacturer>(MANUFACTURER_KEY)!! }
    private val carType by lazy { arguments?.getParcelable<CarType>(CAR_TYPE_KEY)!! }
    private val buildDate by lazy { arguments?.getParcelable<CarBuildDate>(CAR_BUILD_DATE_KEY)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setupViews()
    }

    private fun setUpToolbar() {
        binding.appBarLayout.toolbar.title = getString(R.string.title_summary)
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            navigator.navigateUp()
        }
    }

    private fun setupViews() {
        binding.manufacturer.text = manufacturer.name
        binding.carType.text = carType.name
        binding.carBuildDate.text = buildDate.year
    }

    companion object {
        private const val MANUFACTURER_KEY = "manufacturer"
        private const val CAR_TYPE_KEY = "car_type"
        private const val CAR_BUILD_DATE_KEY = "build_date"

        fun buildArguments(manufacturer: CarManufacturer, carType: CarType, buildDate: CarBuildDate): Bundle {
            return bundleOf(
                MANUFACTURER_KEY to manufacturer,
                CAR_TYPE_KEY to carType,
                CAR_BUILD_DATE_KEY to buildDate,
            )
        }
    }
}
