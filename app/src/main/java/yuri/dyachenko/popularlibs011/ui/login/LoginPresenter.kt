package yuri.dyachenko.popularlibs011.ui.login

import kotlinx.coroutines.*
import yuri.dyachenko.popularlibs011.domain.LoginData
import yuri.dyachenko.popularlibs011.domain.LoginRepo
import yuri.dyachenko.popularlibs011.domain.RESULT_EMPTY_EMAIL

const val SIMULATION_DELAY_MILLIS = 1_000L

class LoginPresenter(private val loginRepo: LoginRepo) : Contract.Presenter(),
    CoroutineScope by MainScope() {

    private var savedData: LoginData? = null
    private var savedSecondPassword: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setState(Contract.State.LOGIN)
    }

    private fun viewSetErrorState(messageId: Int, errorState: Contract.State) {
        viewState.setErrorMessage(messageId)
        viewState.setState(errorState)
    }

    private fun viewSetOkState(messageId: Int, okState: Contract.State) {
        viewState.setOkMessage(messageId)
        viewState.setState(okState)
    }

    private fun viewSetLoadingState() {
        viewState.setOkMessage(MESSAGE_WAIT_CHECKING)
        viewState.setState(Contract.State.LOADING)
    }

    private fun repoDoDelayed(action: () -> (Int?), errorState: Contract.State, okMessageId: Int) {
        viewSetLoadingState()
        launch {
            var result: Int?
            viewState.setState(withContext(Dispatchers.IO) {
                delay(SIMULATION_DELAY_MILLIS)
                result = action()
                getStateByResult(result, errorState)
            })
            viewSetResultMessage(result, okMessageId)
        }
    }

    private fun getStateByResult(result: Int?, errorState: Contract.State) =
        result?.let { errorState } ?: Contract.State.SUCCESS

    private fun viewSetResultMessage(result: Int?, okMessageId: Int) {
        result?.let { viewState.setErrorMessage(result) } ?: viewState.setOkMessage(okMessageId)
    }

    override fun onEnter(data: LoginData) {
        repoDoDelayed(
            { loginRepo.login(data) },
            Contract.State.ERROR_LOGIN,
            MESSAGE_OK_LOGGED_IN
        )
    }

    override fun onRegister(data: LoginData, secondPassword: String) {
        repoDoDelayed(
            { loginRepo.register(data, secondPassword) },
            Contract.State.ERROR_REGISTRATION,
            MESSAGE_OK_REGISTERED
        )
    }

    override fun onExit() {
        viewState.setState(Contract.State.LOGIN)
    }

    override fun onRegistration() {
        viewState.setState(Contract.State.REGISTRATION)
    }

    override fun onErrorLogin() {
        viewState.setState(Contract.State.LOGIN)
    }

    override fun onErrorRegistration() {
        viewState.setState(Contract.State.REGISTRATION)
    }

    override fun onReturn() {
        viewState.setState(Contract.State.LOGIN)
    }

    override fun onTextChanged(data: LoginData, secondPassword: String) {
        savedData = data
        savedSecondPassword = secondPassword
    }

    override fun onForgotPassword(data: LoginData) =
        takeIf { data.email.isBlank() }?.let {
            viewSetErrorState(RESULT_EMPTY_EMAIL, Contract.State.ERROR_LOGIN)
        } ?: viewSetOkState(MESSAGE_OK_PASSWORD_SENT, Contract.State.PASSWORD_SENT)
}