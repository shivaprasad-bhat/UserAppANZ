package `in`.svbneelmane.useranz.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.svbneelmane.useranz.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * ViewModel for managing user list screen state and business logic.
 *
 * Handles fetching users from the use case, managing UI state transitions (Loading, Success, Error),
 * and exposing state as a Flow for UI consumption.
 *
 * @property getUsersUseCase The use case for fetching users
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    private var loadUsersJob: Job? = null

    init {
        loadUsers()
    }

    fun loadUsers(page: Int = 1) {
        loadUsersJob?.cancel()
        loadUsersJob = viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            getUsersUseCase(page).collect { result ->
                result
                    .onSuccess { users ->
                        _uiState.value = UserListUiState.Success(users)
                    }
                    .onFailure { error ->
                        _uiState.value = UserListUiState.Error(
                            error.message ?: "An unknown error occurred"
                        )
                    }
            }
        }
    }
}
