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
package com.kinvey.sample.kitchensink

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @author edwardf
 * @since 2.0
 */
object AndroidUtil {
    /**
     * Hide the keyboard in a given context from the associated View.
     * Usually v will be an `android.widget.EditText`
     *
     * @param context the current activity's context
     * @param v the view object currently holding focus, with an exposed keyboard
     */
    fun hideKeyboard(context: Context, v: View) {
        val imm = context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun toast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * convenience wrapper for toasting from a fragment
     *
     * @param fragment - the visible fragment
     * @param message - the message to be displayed in the toast
     */
    fun toast(fragment: Fragment, message: String?) {
        Toast.makeText(fragment.activity, message, Toast.LENGTH_SHORT).show()
    }
}