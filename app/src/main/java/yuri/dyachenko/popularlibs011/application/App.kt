package yuri.dyachenko.popularlibs011.application

import android.app.Application
import yuri.dyachenko.popularlibs011.impl.LoginListRepoImpl
import yuri.dyachenko.popularlibs011.ui.login.LoginPresenter

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var loginPresenter: LoginPresenter? = null

        fun getLoginPresenter() = loginPresenter ?: synchronized(LoginPresenter::class.java) {
            loginPresenter ?: appInstance?.let {
                LoginPresenter(LoginListRepoImpl())
            } ?: throw NullPointerException()
        }
    }
}