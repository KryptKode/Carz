package com.kryptkode.carz.navigator

import androidx.navigation.NavController
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.ui.carbuilddate.CarBuildDateFragment
import com.kryptkode.carz.ui.cartypes.CarTypesFragment
import com.kryptkode.carz.ui.summary.SummaryFragment
import javax.inject.Inject

class Navigator @Inject constructor(
    private val componentsProvider: NavComponentsProvider,
) {

    private val navController: NavController
        get() = componentsProvider.navController

    fun toCarType(manufacturer: CarManufacturer) {
        navController.navigate(
            R.id.action_global_carTypesFragment,
            CarTypesFragment.buildArguments(manufacturer)
        )
    }

    fun toCarBuildDate(manufacturer: CarManufacturer, carType: CarType) {
        navController.navigate(
            R.id.action_global_carBuildDateFragment,
            CarBuildDateFragment.buildArguments(manufacturer, carType)
        )
    }

    fun toSummary(manufacturer: CarManufacturer, carType: CarType, buildDate: CarBuildDate) {
        navController.navigate(
            R.id.action_global_summaryFragment,
            SummaryFragment.buildArguments(manufacturer, carType, buildDate)
        )
    }

    fun fromCarBuildDateToManufacturer() {
        navController.popBackStack(R.id.manufacturerFragment, false)
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}
