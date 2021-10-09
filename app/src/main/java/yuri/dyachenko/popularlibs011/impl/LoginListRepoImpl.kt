package yuri.dyachenko.popularlibs011.impl

import yuri.dyachenko.popularlibs011.domain.LoginData
import yuri.dyachenko.popularlibs011.domain.LoginRepo

class LoginListRepoImpl : LoginRepo {

    private val list = mutableListOf<LoginData>()

    override fun add(data: LoginData) {
        list.add(data)
    }

    override fun findByEmail(data: LoginData): LoginData? {
        val searchEmail = data.email.lowercase()
        return list.firstOrNull { it.email == searchEmail }
    }
}