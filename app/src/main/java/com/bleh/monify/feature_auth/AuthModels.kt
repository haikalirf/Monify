package com.bleh.monify.feature_auth

data class LoginResult(
    val data: UserData?,
    val errorMessage: String?
)

data class RegisterResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val email: String?,
    val profilePictureUrl: String?
)