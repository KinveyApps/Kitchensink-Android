package com.kinvey.sample.kitchensink

import android.R
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kinvey.android.Client
import com.kinvey.android.model.User

abstract class BaseAppCompatActivity : AppCompatActivity() {

    val isUserLoggedIn: Boolean
        get() = client?.isUserLoggedIn ?: false

    val client: Client<User>?
        get() = App.instance?.client

    val fragmentContainer: Int = R.id.content

    fun replaceFragment(fragment: Fragment, backStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        if (backStack) {
            ft.addToBackStack(fragment::class.java.name)
        }
        ft.replace(fragmentContainer, fragment)
        ft.commitAllowingStateLoss()
    }
}