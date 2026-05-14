package `in`.svbneelmane.useranz.data.mapper

import `in`.svbneelmane.useranz.data.remote.dto.UserDto
import `in`.svbneelmane.useranz.domain.model.User

fun UserDto.toDomain(): User = User(
    id = id,
    email = email,
    firstName = name.substringBefore(' ').ifBlank { name },
    lastName = name.substringAfter(' ', ""),
    avatarUrl = photo,
    company = company,
    username = username,
    address = address,
    zip = zip,
    state = state,
    country = country,
    phone = phone
)

