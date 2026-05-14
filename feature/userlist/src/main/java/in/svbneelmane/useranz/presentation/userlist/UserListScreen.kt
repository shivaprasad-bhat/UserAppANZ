package `in`.svbneelmane.useranz.presentation.userlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.svbneelmane.useranz.presentation.components.ErrorView
import `in`.svbneelmane.useranz.presentation.components.LoadingIndicator
import `in`.svbneelmane.useranz.presentation.components.UserCard

/**
 * Displays a list of users in a scrollable column.
 *
 * Manages different UI states:
 * - Loading: Shows a loading indicator
 * - Success: Displays user cards in a lazy list
 * - Error: Shows error message with retry button
 *
 * @param modifier Compose modifier for layout customization
 * @param viewModel ViewModel providing user list state
 * @param onUserClick Callback invoked when a user card is tapped with user ID
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    viewModel: UserListViewModel = hiltViewModel(),
    onUserClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UserListUiState.Loading -> LoadingIndicator(modifier = modifier)
        is UserListUiState.Error -> ErrorView(
            message = state.message,
            onRetry = { viewModel.loadUsers() },
            modifier = modifier
        )
        is UserListUiState.Success -> {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(state.users, key = { it.id }) { user ->
                    UserCard(
                        user = user,
                        onClick = { onUserClick(user.id) }
                    )
                }
            }
        }
    }
}

