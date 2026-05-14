package `in`.svbneelmane.useranz.presentation.userlist

import `in`.svbneelmane.useranz.domain.model.User
import `in`.svbneelmane.useranz.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val useCase: GetUsersUseCase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads users and updates state to success`() = runTest {
        val users = listOf(sampleUser(1), sampleUser(2))
        whenever(useCase(1)).thenReturn(flowOf(Result.success(users)))

        val viewModel = UserListViewModel(useCase)
        assertTrue(viewModel.uiState.value is UserListUiState.Loading)

        advanceUntilIdle()

        assertEquals(UserListUiState.Success(users), viewModel.uiState.value)
        verify(useCase).invoke(1)
    }

    @Test
    fun `loadUsers forwards page and updates state to success`() = runTest {
        val usersPage3 = listOf(sampleUser(3))
        whenever(useCase(1)).thenReturn(flowOf(Result.success(emptyList())))
        whenever(useCase(3)).thenReturn(flowOf(Result.success(usersPage3)))

        val viewModel = UserListViewModel(useCase)
        advanceUntilIdle()

        viewModel.loadUsers(page = 3)
        advanceUntilIdle()

        assertEquals(UserListUiState.Success(usersPage3), viewModel.uiState.value)
        verify(useCase).invoke(3)
    }

    @Test
    fun `loadUsers updates state to error with throwable message`() = runTest {
        whenever(useCase(1)).thenReturn(
            flowOf(Result.failure(IllegalStateException("Network failed")))
        )

        val viewModel = UserListViewModel(useCase)
        advanceUntilIdle()

        assertEquals(UserListUiState.Error("Network failed"), viewModel.uiState.value)
    }

    @Test
    fun `loadUsers updates state to fallback error when message is null`() = runTest {
        whenever(useCase(1)).thenReturn(flowOf(Result.failure(Exception())))

        val viewModel = UserListViewModel(useCase)
        advanceUntilIdle()

        assertEquals(
            UserListUiState.Error("An unknown error occurred"),
            viewModel.uiState.value
        )
    }

    private fun sampleUser(id: Int): User = User(
        id = id,
        email = "user$id@example.com",
        firstName = "User",
        lastName = "$id",
        avatarUrl = "https://example.com/avatar$id.png",
        company = "Company $id",
        username = "user$id",
        address = "Address $id",
        zip = "100$id",
        state = "State",
        country = "Country",
        phone = "+1-000-000-$id"
    )
}
