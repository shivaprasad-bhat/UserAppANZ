package `in`.svbneelmane.useranz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import `in`.svbneelmane.useranz.presentation.components.ErrorView
import `in`.svbneelmane.useranz.presentation.components.LoadingIndicator
import `in`.svbneelmane.useranz.presentation.userlist.UserListScreen
import `in`.svbneelmane.useranz.presentation.userlist.UserListUiState
import `in`.svbneelmane.useranz.presentation.userlist.UserListViewModel
import `in`.svbneelmane.useranz.presentation.userdetails.UserDetailsScreen
import `in`.svbneelmane.useranz.ui.theme.UserANZTheme

/**
 * Main entry point activity for the UserANZ application.
 *
 * This activity sets up the Compose UI with theme and navigation.
 * It handles the root navigation between user list and user details screens.
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserANZTheme {
                UserAnzRoot()
            }
        }
    }
}

private const val ROUTE_USERS = "users"
private const val ARG_USER_ID = "userId"
private const val ROUTE_USER_DETAILS = "user/{$ARG_USER_ID}"

private fun userDetailsRoute(userId: Int): String = "user/$userId"

/**
 * Root composable for the UserANZ application.
 *
 * Manages the app shell including the main scaffold, top app bar, and navigation host.
 * Handles navigation between list and details screens.
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@Composable
private fun UserAnzRoot() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { UserAppTopBar() }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_USERS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_USERS) {
                UserListScreen(
                    onUserClick = { userId ->
                        navController.navigate(userDetailsRoute(userId))
                    }
                )
            }
            composable(
                route = ROUTE_USER_DETAILS,
                arguments = listOf(navArgument(ARG_USER_ID) { type = NavType.IntType })
            ) { backStackEntry ->
                UserDetailsRoute(navController = navController, userId = backStackEntry.arguments?.getInt(ARG_USER_ID))
            }
        }
    }
}

/**
 * Handles the user details route state and UI.
 *
 * Retrieves the shared ViewModel from the list screen's back stack entry,
 * collects the list state, finds the selected user by ID, and displays their details.
 *
 * @param navController Navigation controller for handling back navigation
 * @param userId The ID of the user to display details for
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@Composable
private fun UserDetailsRoute(navController: NavHostController, userId: Int?) {
    val parentEntry = remember(navController) { navController.getBackStackEntry(ROUTE_USERS) }
    val viewModel: UserListViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UserListUiState.Loading -> LoadingIndicator()
        is UserListUiState.Error -> ErrorView(
            message = state.message,
            onRetry = { viewModel.loadUsers() }
        )
        is UserListUiState.Success -> {
            val selectedUser = state.users.firstOrNull { it.id == userId }
            if (selectedUser == null) {
                ErrorView(
                    message = "User not found",
                    onRetry = { navController.popBackStack() }
                )
            } else {
                UserDetailsScreen(
                    user = selectedUser,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * Renders the app-level top bar with a title.
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserAppTopBar() {
    TopAppBar(title = { Text(text = "User App") })
}
