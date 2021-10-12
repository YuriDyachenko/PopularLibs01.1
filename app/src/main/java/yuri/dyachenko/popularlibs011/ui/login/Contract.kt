package yuri.dyachenko.popularlibs011.ui.login

import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import yuri.dyachenko.popularlibs011.domain.LoginData

const val MESSAGE_OK_LOGGED_IN = 101
const val MESSAGE_WAIT_CHECKING = 102
const val MESSAGE_OK_REGISTERED = 103
const val MESSAGE_OK_PASSWORD_SENT = 104

class Contract {

    enum class State {

        LOGIN, REGISTRATION, ERROR_LOGIN, ERROR_REGISTRATION, LOADING, SUCCESS, PASSWORD_SENT
    }

    interface View : MvpView {

        @AddToEndSingle
        fun setState(state: State)

        @AddToEndSingle
        fun setData(data: LoginData, secondPassword: String)

        @AddToEndSingle
        fun setOkMessage(messageId: Int)

        @AddToEndSingle
        fun setErrorMessage(messageId: Int)
    }

    abstract class Presenter : MvpPresenter<View>() {

        abstract fun onEnter(data: LoginData)
        abstract fun onExit()
        abstract fun onRegister(data: LoginData, secondPassword: String)
        abstract fun onRegistration()
        abstract fun onErrorLogin()
        abstract fun onErrorRegistration()
        abstract fun onReturn()
        abstract fun onTextChanged(data: LoginData, secondPassword: String)
        abstract fun onForgotPassword(data: LoginData)
    }
}