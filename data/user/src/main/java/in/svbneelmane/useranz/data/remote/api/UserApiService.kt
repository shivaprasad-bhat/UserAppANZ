package `in`.svbneelmane.useranz.data.remote.api

import `in`.svbneelmane.useranz.data.remote.dto.UserDto
import retrofit2.http.GET

fun interface UserApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}

