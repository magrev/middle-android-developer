package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        if (map.containsKey(email.trim().toLowerCase())) {
            throw IllegalArgumentException("A user with this email already exists")
        } else {
            return User.makeUser(fullName, email = email, password = password)
                .also { user -> map[user.login] = user }
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        if (!rawPhone.matches(Regex("^((\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$"))) {
            throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
        if (map.containsKey(rawPhone.replace("[^+\\d]".toRegex(), ""))) {
            throw IllegalArgumentException("A user with this phone already exists")
        }
        return User.makeUser(fullName, phone = rawPhone)
            .also { user -> map[user.login] = user }
    }

    private fun getUserByLogin(login: String): User? {
        return if (map.containsKey(login.trim())) {
            map[login.trim()]
        } else {
            map[login.trim().replace("[^+\\d]".toRegex(), "")]
        }
    }

    fun loginUser(login: String, password: String): String? {
        return getUserByLogin(login)?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) {
        getUserByLogin(login)?.resetAccessCode()
    }
}