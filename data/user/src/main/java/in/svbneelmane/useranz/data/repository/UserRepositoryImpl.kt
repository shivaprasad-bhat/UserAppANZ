package `in`.svbneelmane.useranz.data.repository

import `in`.svbneelmane.useranz.data.mapper.toDomain
import `in`.svbneelmane.useranz.data.remote.api.UserApiService
import `in`.svbneelmane.useranz.di.IoDispatcher
import `in`.svbneelmane.useranz.domain.model.User
import `in`.svbneelmane.useranz.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Implementation of [UserRepository] that provides user data from a remote API.
 *
 * This class handles communication with the UserApiService and maps API responses
 * to domain models. All network operations are executed on the IO dispatcher to
 * avoid blocking the main thread.
 *
 * @param api The UserApiService instance used to fetch user data.
 * @param ioDispatcher The CoroutineDispatcher used for IO operations.
 */
class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getUsers(page: Int): Flow<Result<List<User>>> = flow {
        val response = api.getUsers()
        emit(Result.success(response.map { it.toDomain() }))
    }.catch { exception ->
        emit(Result.failure(exception))
    }.flowOn(ioDispatcher)
}
