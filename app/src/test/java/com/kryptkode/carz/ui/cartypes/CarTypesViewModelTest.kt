package com.kryptkode.carz.ui.cartypes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.kryptkode.cardinfofinder.utils.MainCoroutineRule
import com.kryptkode.cardinfofinder.utils.runBlockingTest
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.usecase.GetCarTypeUseCase
import com.kryptkode.carz.ui.cartypes.CarTypesViewModel.CarTypeParams
import com.kryptkode.carz.ui.cartypes.CarTypesViewModel.Companion.PARAM_KEY
import com.kryptkode.carz.utils.MockDataFactory.makeFakeCarType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CarTypesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getCarTypeUseCase: GetCarTypeUseCase

    @MockK(relaxed = true)
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var sut: CarTypesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = CarTypesViewModel(getCarTypeUseCase, savedStateHandle)

        stubSaveStateGetLiveData()
    }

    @Test
    fun `stateReducer when on PagingDataState Error returns error state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarTypesViewState()

        val errorMessage = "Error occurred"

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Error(errorMessage))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isTrue()
        assertThat(result.errorMessage).isEqualTo(errorMessage)
        assertThat(result.types).isEqualTo(initialState.types)
    }

    @Test
    fun `stateReducer when on PagingDataState Loading returns loading state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarTypesViewState()

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Loading)

        assertThat(result.loading).isTrue()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.types).isEqualTo(initialState.types)
    }

    @Test
    fun `stateReducer when on PagingDataState Success returns success state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarTypesViewState()
        val types = listOf(makeFakeCarType(), makeFakeCarType())

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Success(types))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.types).isEqualTo(types)
    }

    @Test
    fun `stateReducer when on PagingDataState EndOfPage returns previous state`() = mainCoroutineRule.runBlockingTest {
        val initialState = CarTypesViewState()
        val result = sut.stateReducer.invoke(initialState, PagingDataState.EndOfPage)

        assertThat(result.loading).isFalse()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.empty).isEqualTo(initialState.empty)
        assertThat(result.types).isEqualTo(initialState.types)
    }

    @Test
    fun `getCarTypes sets savedState handle if not previously set`() {
        val page = 3
        val manufacturer = "he0"
        stubSaveStateGet(CarTypeParams(manufacturer, 2))
        sut.getCarTypes(page)

        verify {
            savedStateHandle.set(PARAM_KEY, CarTypeParams(manufacturer, page))
        }
    }

    @Test
    fun `getCarTypes does not set savedState handle if previously set`() {
        val page = 2
        val manufacturer = "he0"
        stubSaveStateGet(CarTypeParams(manufacturer, page))
        sut.getCarTypes(page)

        verify(inverse = true) {
            savedStateHandle.set(PARAM_KEY, CarTypeParams(manufacturer, page))
        }
    }

    @Test
    fun `refreshData sets savedState with it's previous value`() {
        val page = 2
        val manufacturer = "he0"
        stubSaveStateGet(CarTypeParams(manufacturer, page))

        sut.refreshData()

        verify {
            savedStateHandle.set(PARAM_KEY, CarTypeParams(manufacturer, page))
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun stubSaveStateGetLiveData(params: MutableLiveData<CarTypeParams> = MutableLiveData()) {
        every {
            savedStateHandle.getLiveData<CarTypeParams>(PARAM_KEY)
        } returns params
    }

    private fun stubSaveStateSet() {
        every {
            savedStateHandle.set(any(), any() as CarTypeParams)
        } returns Unit
    }

    private fun stubSaveStateGet(params: CarTypeParams) {
        every {
            savedStateHandle.get<CarTypeParams>(PARAM_KEY)
        } returns params
    }

    private fun stubGetBuildDateUseCase(dataState: PagingDataState<List<CarType>>) {
        every {
            getCarTypeUseCase.execute(any(), any(), any())
        } returns flowOf(dataState)
    }
}
