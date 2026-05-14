package `in`.svbneelmane.useranz.presentation.userlist

import `in`.svbneelmane.useranz.domain.model.User

sealed class UserListUiState {
    data object Loading : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String) : UserListUiState()
}

