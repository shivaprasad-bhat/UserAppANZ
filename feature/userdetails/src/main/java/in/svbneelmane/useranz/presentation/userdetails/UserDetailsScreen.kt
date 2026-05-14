package `in`.svbneelmane.useranz.presentation.userdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import `in`.svbneelmane.useranz.feature.userdetails.R
import `in`.svbneelmane.useranz.domain.model.User

/**
 * Displays detailed information for a selected user.
 *
 * Shows user avatar, name, and detailed properties including email, company,
 * phone, address, and location information in a scrollable column with
 * a back navigation button.
 *
 * @param user The user object containing all detail information
 * @param onBackClick Callback invoked when back button is pressed
 * @param modifier Compose modifier for layout customization
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
@Composable
fun UserDetailsScreen(
    user: User,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(onClick = onBackClick, modifier = Modifier.align(Alignment.Start)) {
            Text(text = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Avatar of ${user.fullName}",
            placeholder = painterResource(id = R.drawable.ic_user_placeholder),
            error = painterResource(id = R.drawable.ic_user_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.fullName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        UserDetailProperty(label = "Email", value = user.email)
        UserDetailProperty(label = "Username", value = user.username)
        UserDetailProperty(label = "Company", value = user.company)
        UserDetailProperty(label = "Phone", value = user.phone)
        UserDetailProperty(label = "Address", value = user.address)
        UserDetailProperty(label = "State", value = user.state)
        UserDetailProperty(label = "Country", value = user.country)
        UserDetailProperty(label = "ZIP", value = user.zip)
    }
}

@Composable
private fun UserDetailProperty(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

