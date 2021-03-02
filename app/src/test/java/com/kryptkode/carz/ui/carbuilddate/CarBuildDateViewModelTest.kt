package com.kryptkode.carz.ui.carbuilddate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.DataState
import com.kryptkode.carz.data.usecase.GetCarBuildDatesUseCase
import com.kryptkode.carz.ui.carbuilddate.CarBuildDateViewModel.CarBuildDateParams
import com.kryptkode.carz.ui.carbuilddate.CarBuildDateViewModel.Companion.PARAM_KEY
import com.kryptkode.carz.utils.MainCoroutineRule
import com.kryptkode.carz.utils.MockDataFactory.makeFakeCarBuildDate
import com.kryptkode.carz.utils.runBlockingTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CarBuildDateViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getCarBuildDates: GetCarBuildDatesUseCase = mockk()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private lateinit var sut: CarBuildDateViewModel

    @Before
    fun setup() {
        sut = CarBuildDateViewModel(getCarBuildDates, savedStateHandle)
    }

    @Test
    fun `stateReducer when on DataState Error returns error state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarBuildDateViewState()

        val errorMessage = "Error occurred"

        val result = sut.stateReducer.invoke(initialState, DataState.Error(errorMessage))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isTrue()
        assertThat(result.errorMessage).isEqualTo(errorMessage)
        assertThat(result.buildDates).isEqualTo(initialState.buildDates)
    }

    @Test
    fun `stateReducer when on DataState Loading returns loading state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarBuildDateViewState()

        val result = sut.stateReducer.invoke(initialState, DataState.Loading)

        assertThat(result.loading).isTrue()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.buildDates).isEqualTo(initialState.buildDates)
    }

    @Test
    fun `stateReducer when on DataState Success returns success state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarBuildDateViewState()
        val buildDates = listOf(makeFakeCarBuildDate(), makeFakeCarBuildDate())

        val result = sut.stateReducer.invoke(initialState, DataState.Success(buildDates))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.buildDates).isEqualTo(buildDates)
    }

    @Test
    fun `getCarBuildDates sets savedState handle if not previously set`() {
        val manufacturer = "0202"
        val type = "332"
        stubSaveStateGet(CarBuildDateParams("", ""))
        sut.getCarBuildDates(manufacturer, type)

        verify {
            savedStateHandle.set(PARAM_KEY, CarBuildDateParams(manufacturer, type))
        }
    }

    @Test
    fun `getCarBuildDates does not set savedState handle if previously set`() {
        val manufacturer = "0202"
        val type = "332"
        stubSaveStateGet(CarBuildDateParams(manufacturer, type))
        sut.getCarBuildDates(manufacturer, type)

        verify(inverse = true) {
            savedStateHandle.set(PARAM_KEY, CarBuildDateParams(manufacturer, type))
        }
    }

    @Test
    fun `refreshData sets savedState with it's previous value`() {
        val manufacturer = "0202"
        val type = "332"
        stubSaveStateGet(CarBuildDateParams(manufacturer, type))

        sut.refreshData()

        verify {
            savedStateHandle.set(PARAM_KEY, CarBuildDateParams(manufacturer, type))
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun stubSaveStateGetLiveData(params: MutableLiveData<CarBuildDateParams> = MutableLiveData()) {
        every {
            savedStateHandle.getLiveData<CarBuildDateParams>(PARAM_KEY)
        } returns params
    }

    private fun stubSaveStateSet() {
        every {
            savedStateHandle.set(any(), any() as CarBuildDateParams)
        } returns Unit
    }

    private fun stubSaveStateGet(params: CarBuildDateParams) {
        every {
            savedStateHandle.get<CarBuildDateParams>(PARAM_KEY)
        } returns params
    }

    private fun stubGetBuildDateUseCase(dataState: DataState<List<CarBuildDate>>) {
        every {
            getCarBuildDates.execute(any(), any())
        } returns flowOf(dataState)
    }
}
