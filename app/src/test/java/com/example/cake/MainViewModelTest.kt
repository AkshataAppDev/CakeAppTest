package com.example.cake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cake.common.Event
import com.example.cake.model.CakeModel
import com.example.cake.network.APIStatus
import com.example.cake.network.CakeApi
import com.example.cake.ui.MainViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val apiService: CakeApi = mock<CakeApi>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(apiService)
    }

    @Test
    fun `when successfully fetch the data from the api, then it should update the value of the status to done`() {

        runBlockingTest {

            whenever(apiService.getCakeList()).thenAnswer { mutableListOf<CakeModel>() }

            viewModel.getCakeItems()

            assertEquals(viewModel.status.value, APIStatus.DONE)
            assertEquals(viewModel.cakeItems.value is List, true)
        }
    }

    @Test
    fun `verify cake list is distinct and sorted`() {
        val cakeItems = listOf<CakeModel>(
            CakeModel(title = "Red Velvet", desc = "white cake dyed red", image = "someurl"),
            CakeModel(title = "Mango cake", desc = "my favourite !!", image = "someurl"),
            CakeModel(title = "Red Velvet", desc = "white cake dyed red", image = "someurl")
        )

        viewModel.cakeItemsSorted.observeForever {}

        viewModel.setCakeItems(cakeItems)

        val expectedCakesList = listOf<CakeModel>(
            CakeModel(title = "Mango cake", desc = "my favourite !!", image = "someurl"),
            CakeModel(
                title = "Red Velvet",
                desc = "white cake dyed red",
                image = "someurl"
            )
        )

        assertEquals(viewModel.cakeItems.value, cakeItems)
        assertEquals(
            viewModel.cakeItemsSorted.value, expectedCakesList
        )
    }

    @Test
    fun `when unable to fetch the data from the api, then it should update the value of the status to error`() {
        runBlockingTest {

            whenever(apiService.getCakeList()).thenAnswer { throw Exception("error") }

            viewModel.getCakeItems()

            assertEquals(viewModel.status.value, APIStatus.ERROR)
        }
    }

    @Test
    fun `on user selects item, it should trigger the event`() {
        val item = mockk<CakeModel>()
        viewModel.userSelectsItem(item)
        val event = viewModel.cakeItemClickedEvent.value
        val selectedItem = viewModel.selectedCakeItem.value
        assertEquals(event is Event, true)
        assertEquals(selectedItem, item)
    }
}