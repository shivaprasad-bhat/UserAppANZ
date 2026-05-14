package `in`.svbneelmane.useranz.domain.usecase

import `in`.svbneelmane.useranz.domain.model.User
import `in`.svbneelmane.useranz.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching a list of users.
 *
 * Implements single responsibility principle by delegating to the repository.
 * Returns a Flow<Result> stream to support reactive data consumption.
 *
 * @property repository The user repository providing data access
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {

    /**
     * Invokes the use case to fetch users from the repository.
     *
     * @param page The page number of users to fetch (default = 1)
     * @return Flow emitting Result containing the list of users or an exception on failure
     */
    operator fun invoke(page: Int = 1): Flow<Result<List<User>>> {
        return repository.getUsers(page)
    }
}
