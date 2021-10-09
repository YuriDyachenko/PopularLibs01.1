package yuri.dyachenko.popularlibs011.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import yuri.dyachenko.popularlibs011.R
import yuri.dyachenko.popularlibs011.databinding.ActivityLoginBinding
import yuri.dyachenko.popularlibs011.domain.*
import yuri.dyachenko.popularlibs011.utils.showOnly

class LoginActivity : AppCompatActivity(), Contract.View {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val presenter by lazy { getLoginPresenter(R.id.container) }

    private val messageMap = mapOf(
        RESULT_EMPTY_EMAIL to R.string.error_text_empty_email,
        RESULT_EMPTY_PASSWORD to R.string.error_text_empty_password,
        RESULT_EMPTY_SECOND_PASSWORD to R.string.error_text_empty_repeat_password,
        RESULT_DIFFERENT_PASSWORDS to R.string.error_text_different_passwords,
        RESULT_EMAIL_NOT_FOUND to R.string.error_text_email_not_found,
        RESULT_WRONG_PASSWORD to R.string.error_text_wrong_password,
        RESULT_LOGIN_IS_BUSY to R.string.error_text_login_is_busy,
        MESSAGE_OK_LOGGED_IN to R.string.ok_logged_in,
        MESSAGE_OK_REGISTERED to R.string.ok_registered,
        MESSAGE_OK_PASSWORD_SENT to R.string.ok_password_sent,
        MESSAGE_WAIT_CHECKING to R.string.wait_checking
    )

    private val clickMap by lazy {
        with(binding) {
            mapOf(
                enterButton to { presenter.onEnter(gatherData()) },
                exitButton to { presenter.onExit() },
                registrationButton to { presenter.onRegistration() },
                registerButton to { presenter.onRegister(gatherData(), gatherSecondPassword()) },
                errorLoginButton to { presenter.onErrorLogin() },
                errorRegistrationButton to { presenter.onErrorRegistration() },
                forgotPasswordButton to { presenter.onForgotPassword(gatherData()) },
                returnButton to { presenter.onReturn() }
            )
        }
    }

    private val loginViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                emailInput,
                passwordInput,
                enterButton,
                registrationButton,
                forgotPasswordButton
            )
        }
    }

    private val registrationViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                emailInput,
                passwordInput,
                secondPasswordInput,
                registerButton,
                returnButton
            )
        }
    }

    private val errorLoginViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                errorTextView,
                errorLoginButton
            )
        }
    }

    private val errorRegistrationViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                errorTextView,
                errorRegistrationButton
            )
        }
    }

    private val loadingViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                okTextView,
                progressBar
            )
        }
    }

    private val successViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                okTextView,
                exitButton
            )
        }
    }

    private val passwordSentViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                welcomeTextView,
                okTextView,
                returnButton
            )
        }
    }

    private val stateViewsMap by lazy {
        mapOf(
            Contract.State.LOGIN to loginViews,
            Contract.State.REGISTRATION to registrationViews,
            Contract.State.ERROR_LOGIN to errorLoginViews,
            Contract.State.ERROR_REGISTRATION to errorRegistrationViews,
            Contract.State.LOADING to loadingViews,
            Contract.State.SUCCESS to successViews,
            Contract.State.PASSWORD_SENT to passwordSentViews
        )
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            presenter.onTextChanged(gatherData(), gatherSecondPassword())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        presenter.onAttach(this)

        initButtons()
        initEdits()
    }

    private fun initButtons() {
        for ((button, function) in clickMap) {
            button.setOnClickListener { run(function) }
        }
    }

    private fun initEdits() = with(binding) {
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        secondPasswordEditText.addTextChangedListener(textWatcher)
    }

    override fun onDestroy() {
        presenter.onDetach()
        _binding = null
        super.onDestroy()
    }

    private fun gatherData() = LoginData(
        binding.emailEditText.text.toString(),
        binding.passwordEditText.text.toString()
    )

    private fun gatherSecondPassword() = binding.secondPasswordEditText.text.toString()

    override fun setState(state: Contract.State) {
        binding.container.showOnly(stateViewsMap.getOrDefault(state, loginViews))
    }

    override fun setData(data: LoginData, secondPassword: String) = with(binding) {
        emailEditText.setText(data.email)
        passwordEditText.setText(data.password)
        secondPasswordEditText.setText(secondPassword)
    }

    override fun setOkMessage(messageId: Int) {
        binding.okTextView.text =
            getString(messageMap.getOrDefault(messageId, R.string.ok_text_unknown))
    }

    override fun setErrorMessage(messageId: Int) {
        binding.errorTextView.text =
            getString(messageMap.getOrDefault(messageId, R.string.error_text_unknown))
    }
}
