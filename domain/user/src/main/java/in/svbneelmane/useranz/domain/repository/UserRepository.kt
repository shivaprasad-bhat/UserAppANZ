package `in`.svbneelmane.useranz.domain.repository

import `in`.svbneelmane.useranz.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(page: Int = 1): Flow<Result<List<User>>>
}
