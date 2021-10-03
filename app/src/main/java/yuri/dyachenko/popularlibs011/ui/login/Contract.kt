package yuri.dyachenko.popularlibs011.ui.login

import yuri.dyachenko.popularlibs011.domain.LoginData

const val MESSAGE_OK_LOGGED_IN = 101
const val MESSAGE_WAIT_CHECKING = 102
const val MESSAGE_OK_REGISTERED = 103
const val MESSAGE_OK_PASSWORD_SENT = 104

class Contract {
    enum class State {
        LOGIN, REGISTRATION, ERROR_LOGIN, ERROR_REGISTRATION, LOADING, SUCCESS, PASSWORD_SENT
    }

    interface View {
        fun setState(state: State)
        fun setData(data: LoginData, secondPassword: String)
        fun setOkMessage(messageId: Int)
        fun setErrorMessage(messageId: Int)
    }

    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
        fun onEnter(data: LoginData)
        fun onExit()
        fun onRegister(data: LoginData, secondPassword: String)
        fun onRegistration()
        fun onErrorLogin()
        fun onErrorRegistration()
        fun onReturn()
        fun onTextChanged(data: LoginData, secondPassword: String)
        fun onForgotPassword()
    }
}