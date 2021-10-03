package yuri.dyachenko.popularlibs011.domain

const val RESULT_EMPTY_EMAIL = 1
const val RESULT_EMPTY_PASSWORD = 2
const val RESULT_EMPTY_SECOND_PASSWORD = 3
const val RESULT_DIFFERENT_PASSWORDS = 4
const val RESULT_EMAIL_NOT_FOUND = 5
const val RESULT_WRONG_PASSWORD = 6
const val RESULT_LOGIN_IS_BUSY = 7

interface LoginRepo {

    fun add(data: LoginData)
    fun findByEmail(data: LoginData): LoginData?

    fun checkLogin(data: LoginData) = with(data) {
        when {
            email.isBlank() -> RESULT_EMPTY_EMAIL
            password.isBlank() -> RESULT_EMPTY_PASSWORD
            else -> null
        }
    }

    fun checkLogin(data: LoginData, secondPassword: String) =
        checkLogin(data) ?: with(data) {
            when {
                secondPassword.isBlank() -> RESULT_EMPTY_SECOND_PASSWORD
                password != secondPassword -> RESULT_DIFFERENT_PASSWORDS
                else -> null
            }
        }

    fun login(data: LoginData) =
        checkLogin(data) ?: findByEmail(data)?.let {
            it.takeIf { it.password != data.password }?.let { RESULT_WRONG_PASSWORD }
        } ?: RESULT_EMAIL_NOT_FOUND

    fun register(data: LoginData, secondPassword: String) =
        checkLogin(data, secondPassword) ?: findByEmail(data)?.let { RESULT_LOGIN_IS_BUSY } ?: let {
            add(LoginData(data.email.lowercase(), data.password))
            null
        }
}