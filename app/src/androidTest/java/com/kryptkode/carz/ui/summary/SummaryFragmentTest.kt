package com.kryptkode.carz.ui.summary

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.kryptkode.carz.R
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.navigator.Navigator
import com.kryptkode.carz.utils.getString
import com.kryptkode.carz.utils.launchFragmentInHiltContainer
import com.kryptkode.carz.utils.withToolbarTitle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import javax.inject.Inject
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SummaryFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @MockK
    lateinit var navigator: Navigator

    @Inject
    lateinit var mockWebServer: MockWebServer

    private val args = SummaryFragment.buildArguments(
        CarManufacturer("020", "Toyota"),
        CarType("221", "Hiace"),
        CarBuildDate("21", "2022")
    )

    @Before
    fun init() {
        MockKAnnotations.init(this)
        hiltRule.inject()
        mockWebServer.start()
        stubNavigatorToCarType()
    }

    @Test
    fun correctTitleShown() {

        launchFragmentInHiltContainer<SummaryFragment>(args)

        onView(isAssignableFrom(Toolbar::class.java))
            .check(matches(withToolbarTitle(getString(R.string.title_summary))))
    }

    private fun stubNavigatorToCarType() {
        every {
            navigator.toCarType(any())
        } returns Unit
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        unmockkAll()
    }
}
