package yuri.dyachenko.popularlibs011.ui.login

import kotlinx.coroutines.*
import yuri.dyachenko.popularlibs011.domain.LoginData
import yuri.dyachenko.popularlibs011.domain.LoginRepo

const val SIMULATION_DELAY_MILLIS = 2_000L

class LoginPresenter(private val loginRepo: LoginRepo) : Contract.Presenter,
    CoroutineScope by MainScope() {

    private var view: Contract.View? = null
    private var savedData: LoginData? = null
    private var savedSecondPassword: String? = null
    private var savedState: Contract.State? = null
    private var savedErrorMessageId: Int? = null
    private var savedOkMessageId: Int? = null

    override fun onAttach(view: Contract.View) {
        this.view = view
        restoreAll()
    }

    private fun restoreAll() = view?.apply {
        viewSetState(savedState ?: Contract.State.LOGIN)
        savedData?.let { setData(it, savedSecondPassword ?: "") }
        savedOkMessageId?.let { setOkMessage(it) }
        savedErrorMessageId?.let { setErrorMessage(it) }
    }

    override fun onDetach() {
        view = null
    }

    private fun viewSetState(state: Contract.State) = view?.apply {
        savedState = state
        setState(state)
    }

    private fun viewSetError(messageId: Int) = view?.apply {
        savedErrorMessageId = messageId
        setErrorMessage(messageId)
    }

    private fun viewSetOk(messageId: Int) = view?.apply {
        savedOkMessageId = messageId
        setOkMessage(messageId)
    }

    private fun viewSetLoadingState() {
        viewSetOk(MESSAGE_WAIT_CHECKING)
        viewSetState(Contract.State.LOADING)
    }

    private fun repoDoDelayed(action: () -> (Int?), errorState: Contract.State, okMessageId: Int) {
        viewSetLoadingState()
        launch {
            var result: Int?
            viewSetState(withContext(Dispatchers.IO) {
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
        result?.let { viewSetError(result) } ?: viewSetOk(okMessageId)
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
        viewSetState(Contract.State.LOGIN)
    }

    override fun onRegistration() {
        viewSetState(Contract.State.REGISTRATION)
    }

    override fun onErrorLogin() {
        viewSetState(Contract.State.LOGIN)
    }

    override fun onErrorRegistration() {
        viewSetState(Contract.State.REGISTRATION)
    }

    override fun onReturn() {
        viewSetState(Contract.State.LOGIN)
    }

    override fun onTextChanged(data: LoginData, secondPassword: String) {
        savedData = data
        savedSecondPassword = secondPassword
    }

    override fun onForgotPassword() {
        viewSetOk(MESSAGE_OK_PASSWORD_SENT)
        viewSetState(Contract.State.PASSWORD_SENT)
    }
}