package com.aeroshi.movies.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aeroshi.movies.model.repository.MovieRepository
import com.aeroshi.movies.util.Constants.Companion.KEY
import com.aeroshi.movies.util.ErrorType
import com.aeroshi.movies.util.StringUtil
import com.aeroshi.movies.util.TrampolineSchedulerProvider
import io.reactivex.Single
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class HomeViewModelTest {

    companion object {
        private var mJsonMovies: String? = null


        @BeforeClass
        @JvmStatic
        fun executeOnce() {
            this::class.java.classLoader?.let { classLoader ->
                mJsonMovies = classLoader.getResource("json/moviesReturn.json").readText()
            }
        }
    }

    @get:Rule
    var mRule: TestRule = InstantTaskExecutorRule()

    private val context = Mockito.mock(Context::class.java)


    @Mock
    lateinit var mMoviesRepository: MovieRepository


    private lateinit var mViewModel: HomeViewModel


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mViewModel =
            HomeViewModel(
                mMoviesRepository,
                TrampolineSchedulerProvider()
            )
    }

    @Test
    fun HomeViewModel_doMovies_deve_retornar_sucesso() {
        // Preparing
        val fakeJsonReturn = mJsonMovies
            ?: StringUtil.EMPTY

        // Mock
        Mockito.`when`(mMoviesRepository.doMovies(KEY, 20))
            .thenReturn(Single.just(fakeJsonReturn))

        // Call
        mViewModel.doMovies(context)

        // Assert
        verify(mMoviesRepository, times(1)).doMovies(KEY, 20)

        assertEquals(true, mViewModel.mLoading.value)
        assertEquals(ErrorType.NONE, mViewModel.mMoviesResult.value?.second)
        Assert.assertNotNull(mViewModel.mMoviesResult.value?.first)
    }

}