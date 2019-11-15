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

import android.R.id
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kinvey.android.Client
import com.kinvey.sample.kitchensink.App

/**
 * @author edwardf
 * @since 2.0
 */
class LoginActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(id.content, LoginFragment())
        ft.commit()
    }

    public override fun onPause() {
        super.onPause()
    }

    private val isUserLoggedIn: Boolean
        private get() = client.isUserLoggedIn

    private val client: Client<*>
        private get() = (applicationContext as App).client
}