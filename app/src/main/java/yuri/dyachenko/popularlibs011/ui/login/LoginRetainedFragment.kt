package yuri.dyachenko.popularlibs011.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import yuri.dyachenko.popularlibs011.R
import yuri.dyachenko.popularlibs011.impl.LoginListRepoImpl

class LoginRetainedFragment : Fragment() {

    private var loginPresenter: LoginPresenter? = null

    fun getLoginPresenter() = loginPresenter
        ?: let {
            loginPresenter = LoginPresenter(LoginListRepoImpl())
            loginPresenter!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_retained, container, false)
    }

    companion object {
        const val TAG = "PRESENTER_STORAGE"
        fun newInstance() = LoginRetainedFragment()
    }
}

fun FragmentActivity.getLoginPresenter(containerId: Int): LoginPresenter {
    var fragment =
        supportFragmentManager.findFragmentByTag(LoginRetainedFragment.TAG) as LoginRetainedFragment?
    if (fragment == null) {
        fragment = LoginRetainedFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(containerId, fragment, LoginRetainedFragment.TAG)
            .commit()
    }
    return fragment.getLoginPresenter()
}