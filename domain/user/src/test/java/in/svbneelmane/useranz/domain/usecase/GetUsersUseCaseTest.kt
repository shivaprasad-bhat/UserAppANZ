package `in`.svbneelmane.useranz.domain.usecase

import `in`.svbneelmane.useranz.domain.model.User
import `in`.svbneelmane.useranz.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetUsersUseCaseTest {

    private val repository: UserRepository = mock()
    private val useCase = GetUsersUseCase(repository)

    @Test
    fun `invoke with default page delegates to repository and returns success`() = runTest {
        val expectedUsers = listOf(sampleUser(1), sampleUser(2))
        whenever(repository.getUsers(1)).thenReturn(flowOf(Result.success(expectedUsers)))

        val result = useCase().first()

        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull())
        verify(repository).getUsers(1)
    }

    @Test
    fun `invoke with explicit page forwards correct page to repository`() = runTest {
        val expectedUsers = listOf(sampleUser(5))
        whenever(repository.getUsers(2)).thenReturn(flowOf(Result.success(expectedUsers)))

        val result = useCase(page = 2).first()

        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull())
        verify(repository).getUsers(2)
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        whenever(repository.getUsers(1)).thenReturn(flowOf(Result.success(emptyList())))

        val result = useCase().first()

        assertTrue(result.isSuccess)
        assertEquals(emptyList<User>(), result.getOrNull())
    }

    @Test
    fun `invoke propagates failure from repository`() = runTest {
        val exception = RuntimeException("API error")
        whenever(repository.getUsers(1)).thenReturn(flowOf(Result.failure(exception)))

        val result = useCase().first()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke propagates failure for explicit page`() = runTest {
        val exception = IllegalStateException("Timeout on page 3")
        whenever(repository.getUsers(3)).thenReturn(flowOf(Result.failure(exception)))

        val result = useCase(page = 3).first()

        assertTrue(result.isFailure)
        assertEquals("Timeout on page 3", result.exceptionOrNull()?.message)
        verify(repository).getUsers(3)
    }

    private fun sampleUser(id: Int) = User(
        id = id,
        email = "u$id@test.com",
        firstName = "First$id",
        lastName = "Last$id",
        avatarUrl = "https://example.com/$id.png",
        company = "Co$id",
        username = "user$id",
        address = "Addr$id",
        zip = "ZIP$id",
        state = "State$id",
        country = "Country$id",
        phone = "000-$id"
    )
}
