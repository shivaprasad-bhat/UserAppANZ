package `in`.svbneelmane.useranz.domain.model

/**
 * Domain model representing a user in the application.
 *
 * Contains all user information fetched from the API including profile,
 * contact, and location details. Provides computed [fullName] property
 * by combining first and last name.
 *
 * @property id Unique identifier for the user
 * @property email User's email address
 * @property firstName User's first name
 * @property lastName User's last name
 * @property avatarUrl URL to the user's profile image
 * @property company Name of the company the user works for
 * @property username User's unique username/handle
 * @property address Street address of the user
 * @property zip Postal/ZIP code
 * @property state State/Province of residence
 * @property country Country of residence
 * @property phone Contact phone number
 *
 * @author shivaprasad-bhat
 * @created May 14, 2026
 */
data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val company: String,
    val username: String,
    val address: String,
    val zip: String,
    val state: String,
    val country: String,
    val phone: String
) {
    /**
     * Computed property that returns the full name by combining first and last names.
     * Returns only non-blank parts separated by space.
     */
    val fullName: String
        get() = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")
}

