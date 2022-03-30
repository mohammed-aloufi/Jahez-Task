package com.example.jahez_task.presentation.home

import app.cash.turbine.test
import com.example.jahez_task.CoroutinesTestRule
import com.example.jahez_task.TestDispatchers
import com.example.jahez_task.base.BaseViewModel
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.State
import com.example.jahez_task.domain.repository.Repository
import com.example.jahez_task.domain.usecase.GetAllRestaurantsUseCase
import com.example.jahez_task.domain.usecase.IsLoggedInUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var isLoggedInUseCase: IsLoggedInUseCase
    private lateinit var getAllRestaurantsUseCase: GetAllRestaurantsUseCase

    private lateinit var testDispatchers: TestDispatchers
    private lateinit var mBaseViewModel: BaseViewModel
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        isLoggedInUseCase = IsLoggedInUseCase(repository)
        getAllRestaurantsUseCase = GetAllRestaurantsUseCase(repository)
        testDispatchers = TestDispatchers()

        homeViewModel = HomeViewModel(
            getAllRestaurantsUseCase,
            isLoggedInUseCase,
            testDispatchers
        )
        mBaseViewModel = homeViewModel
    }

    @Test
    fun userNotLogged_returnsFalse() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(repository.isUserLoggedIn()).thenReturn(flow { emit(false) })
        val job = launch {
            homeViewModel.loggedState.test {
                val emission = awaitItem()
                assertThat(emission).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }
        homeViewModel.isLogged()
        job.join()
        job.cancel()
    }

    @Test
    fun userLogged_returnsTrue() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(repository.isUserLoggedIn()).thenReturn(flow { emit(true) })
        val job = launch {
            homeViewModel.loggedState.test {
                val emission = awaitItem()
                assertThat(emission).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }
        homeViewModel.isLogged()
        job.join()
        job.cancel()
    }

    @Test
    fun gettingRestaurants_IsLoading() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(repository.getAllRestaurants()).thenReturn(flow { emit(Resource.Loading()) })
        homeViewModel.getAllRestaurants()
        val job = launch {
            mBaseViewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.isLoading).isTrue()
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()
    }

    @Test
    fun gettingRestaurants_successWithData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val restaurants = listOf(
            Restaurant(
                0,
                "Kudu",
                "Enjoy fast delivery from Jahez. Order now, or schedule your order any time you want",
                "05:30 AM - 04:30 AM",
                "https://jahez-other-oniiphi8.s3.eu-central-1.amazonaws.com/1.jpg",
                "8",
                "1.8",
                false
            )
        )
        Mockito.`when`(repository.getAllRestaurants()).thenReturn(flow { emit(Resource.Success(restaurants)) })
        homeViewModel.getAllRestaurants()

        val job = launch {
            homeViewModel.restaurants.test {
                val emission = awaitItem()
                assertThat(emission.size).isEqualTo(1)
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()
    }

    @Test
    fun gettingRestaurants_successWithoutData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val restaurants = emptyList<Restaurant>()
        Mockito.`when`(repository.getAllRestaurants()).thenReturn(flow { emit(Resource.Success(restaurants)) })
        homeViewModel.getAllRestaurants()

        val job = launch {
            homeViewModel.restaurants.test {
                val emission = awaitItem()
                assertThat(emission.size).isEqualTo(0)
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()
    }

    @Test
    fun gettingRestaurants_returnsError() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(repository.getAllRestaurants()).thenReturn(flow { emit(Resource.Error("Unknown Error")) })
        homeViewModel.getAllRestaurants()
        val job = launch {
            mBaseViewModel.state.test {
                val emission = awaitItem()
                assertThat(emission.message).isNotEmpty()
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()
    }
}