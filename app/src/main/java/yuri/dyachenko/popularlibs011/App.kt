package yuri.dyachenko.popularlibs011

import android.app.Application
import yuri.dyachenko.popularlibs011.domain.LoginRepo
import yuri.dyachenko.popularlibs011.impl.LoginListRepoImpl

class App : Application() {

    companion object {
        val loginRepo: LoginRepo = LoginListRepoImpl()
    }
}