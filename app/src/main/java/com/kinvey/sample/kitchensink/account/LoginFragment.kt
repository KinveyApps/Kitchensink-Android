/**
 * Copyright (c) 2019 Kinvey Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.kinvey.sample.kitchensink.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.TextView.OnEditorActionListener
import com.kinvey.android.model.User
import com.kinvey.android.store.UserStore
import com.kinvey.java.core.KinveyClientCallback
import com.kinvey.sample.kitchensink.KitchenSinkActivity
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber
import java.io.IOException

/**
 * @author edwardf
 * @since 2.0
 */
class LoginFragment : UseCaseFragment(), OnClickListener, OnCheckedChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override val viewID = R.layout.fragment_login

    override val title = "Login"

    override fun initViews(v: View) {
        loginBtn?.setOnClickListener(this)
        loginAnonCheckbox?.setOnCheckedChangeListener(this)
        addEditListeners()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.loginBtn) {
            val clientCallback: KinveyClientCallback<User> = object : KinveyClientCallback<User> {
                override fun onSuccess(result: User) {
                    if (activity == null) { return }
                    val text: CharSequence = "Logged in " + result["username"].toString() + "."
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                    loggedIn()
                }
                override fun onFailure(error: Throwable) {
                    if (activity == null) { return }
                    val text: CharSequence = "Something went wrong -> $error"
                    val toast: Toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    toast.show()
                }
            }
            try {
                client?.let { clt ->
                    if (loginAnonCheckbox?.isChecked == true) {
                        UserStore.login(clt, clientCallback)
                    } else {
                        UserStore.login(etLogin?.text.toString(), etPassword?.text.toString(), clt, clientCallback)
                    }
                }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    protected fun addEditListeners() {
        loginBtn?.isEnabled = validateInput()
        etLogin?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                loginBtn?.isEnabled = validateInput()
            }
        })
        etLogin?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if ((actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE ||
                   (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER))
                    && etLogin?.text?.length ?: 0 < MIN_USERNAME_LENGTH) {
                    val text: CharSequence = "User name must contain at least $MIN_USERNAME_LENGTH characters"
                    Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                }
                false
            })
        etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable?) { loginBtn?.isEnabled = validateInput() }
        })
        etPassword?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if ((actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER))
                     && etPassword?.text?.length ?: 0 < MIN_USERNAME_LENGTH) {
                    val text: CharSequence = "Password must contain at least $MIN_PASSWORD_LENGTH characters"
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener true
                }
                false
            })
    }

    fun validateInput(): Boolean {
        return (etLogin?.toString()?.length ?: 0 >= MIN_USERNAME_LENGTH &&
                etPassword?.text?.length ?: 0 >= MIN_PASSWORD_LENGTH) || loginAnonCheckbox?.isChecked == true
    }

    private fun loggedIn() {
        if (activity?.currentFocus != null) {
            val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
        startKitchenSinkActivity()
        activity?.finish()
    }

    private fun startKitchenSinkActivity() {
        val i = Intent(context, KitchenSinkActivity::class.java)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_login, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_up -> {
                replaceFragment(RegisterFragment(), true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        etLogin?.setText("")
        etLogin?.isEnabled = !isChecked
        etPassword?.setText("")
        etPassword?.isEnabled = !isChecked
    }

    companion object {
        const val MIN_USERNAME_LENGTH = 3
        const val MIN_PASSWORD_LENGTH = 3
    }
}