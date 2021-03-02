package com.kryptkode.carz.ui.manufacturer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.usecase.GetManufacturersUseCase
import com.kryptkode.carz.ui.manufacturer.ManufacturerViewModel.Companion.PARAM_KEY
import com.kryptkode.carz.ui.manufacturer.ManufacturerViewModel.Params
import com.kryptkode.carz.utils.MainCoroutineRule
import com.kryptkode.carz.utils.MockDataFactory.makeFakeCarManufacturer
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

class ManufacturerViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getManufacturersUseCase: GetManufacturersUseCase = mockk()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private lateinit var sut: ManufacturerViewModel

    @Before
    fun setup() {
        sut = ManufacturerViewModel(getManufacturersUseCase, savedStateHandle)
        stubSaveStateGetLiveData()
    }

    @Test
    fun `stateReducer when on PagingDataState Error returns error state`() = mainCoroutineRule.runBlockingTest {
        val initialState = ManufacturerViewState()

        val errorMessage = "Error occurred"

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Error(errorMessage))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isTrue()
        assertThat(result.errorMessage).isEqualTo(errorMessage)
        assertThat(result.manufacturers).isEqualTo(initialState.manufacturers)
    }

    @Test
    fun `stateReducer when on PagingDataState Loading returns loading state`() = mainCoroutineRule.runBlockingTest {
        val initialState = ManufacturerViewState()

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Loading)

        assertThat(result.loading).isTrue()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.manufacturers).isEqualTo(initialState.manufacturers)
    }

    @Test
    fun `stateReducer when on PagingDataState Success returns success state`() = mainCoroutineRule.runBlockingTest {
        val initialState = ManufacturerViewState()
        val manufacturers = listOf(makeFakeCarManufacturer(), makeFakeCarManufacturer())

        val result = sut.stateReducer.invoke(initialState, PagingDataState.Success(manufacturers))

        assertThat(result.loading).isFalse()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.manufacturers).isEqualTo(manufacturers)
    }

    @Test
    fun `stateReducer when on PagingDataState EndOfPage returns previous state`() = mainCoroutineRule.runBlockingTest {
        val initialState = ManufacturerViewState()
        val result = sut.stateReducer.invoke(initialState, PagingDataState.EndOfPage)

        assertThat(result.loading).isFalse()
        assertThat(result.error).isFalse()
        assertThat(result.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(result.empty).isEqualTo(initialState.empty)
        assertThat(result.manufacturers).isEqualTo(initialState.manufacturers)
    }

    @Test
    fun `getManufacturers sets savedState handle if not previously set`() {
        val page = 2
        stubSaveStateGet(Params(0))
        sut.getManufacturers(page)

        verify {
            savedStateHandle.set(PARAM_KEY, Params(page))
        }
    }

    @Test
    fun `getManufacturers does not set savedState handle if previously set`() {
        val page = 2
        stubSaveStateGet(Params(page))
        sut.getManufacturers(page)

        verify(inverse = true) {
            savedStateHandle.set(PARAM_KEY, Params(page))
        }
    }

    @Test
    fun `refreshData sets savedState with it's previous value`() {
        val page = 2
        stubSaveStateGet(Params(page))

        sut.refreshData()

        verify {
            savedStateHandle.set(PARAM_KEY, Params(page))
        }
    }

    @Test
    fun `refreshAllData sets savedState refresh to true`() {

        sut.refreshAllData()

        verify {
            savedStateHandle.set(PARAM_KEY, Params(refresh = true))
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun stubSaveStateGetLiveData(params: MutableLiveData<Params> = MutableLiveData()) {
        every {
            savedStateHandle.getLiveData<Params>(PARAM_KEY)
        } returns params
    }

    private fun stubSaveStateSet() {
        every {
            savedStateHandle.set(any(), any() as Params)
        } returns Unit
    }

    private fun stubSaveStateGet(params: Params) {
        every {
            savedStateHandle.get<Params>(PARAM_KEY)
        } returns params
    }

    private fun stubGetBuildDateUseCase(dataState: PagingDataState<List<CarManufacturer>>) {
        every {
            getManufacturersUseCase.execute(any(), any())
        } returns flowOf(dataState)
    }
}
